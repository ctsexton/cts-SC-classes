ControllerViewManager {
  var <focus, currentView, views;

  *new { 
    ^super.newCopyArgs().init();
  }

  init {
    views = [];
  }

  currentView {
    // Get the currently focused view
    ^views[focus];
  }

  focus_ { |viewId|
    // Set the currently focused view and 
    // trigger a view refresh
    focus = viewId;
    this.currentView.refresh();
    ^focus;
  }

  routeCtrlData { |event|
    // Controller hardware should call
    // this method and pass in event data
    // to be routed to the responder functions
    // of the currently selected view
    this.currentView.respond(event);
  } 

  requestRender { |viewId, renderAction|
    // Views must ask permission to execute renderActions
    if (viewId == focus, renderAction);
  }

  registerView { |view|
    // Each new view is added to the view list
    var viewId = views.size;
    views = views.add(view);
    ^viewId;
  }
}
