(function (root, factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD. Register as an anonymous module.
		define([], factory);
	} else if (typeof module === 'object' && module.exports) {
		// Node. Does not work with strict CommonJS, but
		// only CommonJS-like environments that support module.exports,
		// like Node.
		module.exports = factory();
	} else {
		// Browser globals (root is window)
		root.JsBridge = factory(root);
	}

}(typeof window == "undefined" ? this : window, function (win) {
	JsBridge = {
		init: function (func) {
			if (window._JSNativeBridge) {
				func(JsBridge);
			} else {
				document.addEventListener(
					'JsBridgeInit',
					function (event) {
						func(JsBridge);
					}
				);
			}
			return this;
		},

		callJavaAsync: function (methodName, params, cb) {
			if (!window._JSNativeBridge) {
				//JS not be injected success
				cb({
					status: "-1",
					msg: "window._JSNativeBridge is undefined"
				}, {});
				return;
			}

			try {
				window._JSNativeBridge._doSendRequest(methodName, params, cb);
			} catch (e) {
				cb({ status: "-1", msg: e }, {});
			}
		},

		callJavaSync: function (methodName, params) {
			if (!window._JSNativeBridge) {
				throw new Error( "error:JS not be injected success");
			}

			var result;
			result = window._JSNativeBridge._doSendRequestSync(methodName, params);
			return result && result.result;
		},

		registerHandler: function (methodName, func) {
			window._JSNativeBridge._registerHandler(methodName, func);
		}
	
	};
	JsBridge.BUILDIN_EVENTS = {
        BACKPRESS: "backpress",
        KEYPRESS: "keypress",
    }
    JsBridge._eventHandlerPool = {

    }
    function eventDispatch(params) {
        var event = params.event;
        if (!event || !event.name) {
            return;
        }
        var eventHandlers = JsBridge._eventHandlerPool[event.name]
        if (eventHandlers && eventHandlers.length > 0) {
            var i = 0;
            for (i = 0; i < eventHandlers.length; i++) {
                var eventHandler = eventHandlers[i];
                if (eventHandler && typeof eventHandler == "function") {
                    var ret = false;
                    try {
                        ret = eventHandler(event)
                    } catch (error) {
                        console.error(error)
                    }
                    if (ret) {
                        return;
                    }
                }
            }
        }
    }
    JsBridge.init(function () {
        JsBridge.registerHandler("onNativeEvent", eventDispatch)
    })
	return JsBridge;
}));
