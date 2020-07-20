ControllerView {
  var state,
    <>renderers,
    <>responders,
    <>defaultResponder,
    <>defaultRenderer,
    <>responderIdField,
    <>manager,
    <>id;

  *new { 
    ^super.newCopyArgs().init();
  }

  init {
    state = ();
    responders = ();
    renderers = ();
    defaultResponder = { |event| event.postln };
    defaultRenderer = { |target, input| [target, input].postln };
    responderIdField = \id;
  }

  refresh {
    state.keysValuesDo({ |target, data|
      var renderAction = this.selectRenderer(data.rendererId);
      manager.requestRender(id, { renderAction.value(target, data.input) });
    })
  }

  render { |target, input, rendererId|
    var renderAction = this.selectRenderer(rendererId);
    state[target] = ( input: input, rendererId: rendererId );
    manager.requestRender(id, { renderAction.value(target, input) });
  }

  respond { |event|
    this.selectResponder(event).value(event);
  }

  selectRenderer { |rendererId|
    var renderer;
    if (rendererId.notNil, {
      renderer = renderers[rendererId];
    }, {
      renderer = defaultRenderer;
    });
    ^renderer;
  }

  selectResponder { |event|
    var responder;
    var responderId = event[responderIdField];
    if (responderId.notNil, {
      responder = responders[responderId];
    }, {
      responder = defaultResponder;
    });
    ^responder;
  }
}
