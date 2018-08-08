(function (JsBridge, factory) {
    factory(JsBridge)
})(JsBridge, function (JsBridge) {
    /**
     * 打开窗口
     * @param {object} params 
     * @param {string} params.name
     * @param {string} params.url
     */
    JsBridge.openWin = function (params) {
        this.callJavaSync("openWin", params);
    }
    /**
     * 
     * @param {object} params 
     * @param {string} params.msg
     */
    JsBridge.toast = function (params) {
        return this.callJavaSync("toast", params);
    }
    /**
     * 同步获取状态栏高度，如果不支持沉浸式，返回0
     */
    JsBridge.statusBarHeight = function () {
        return this.callJavaSync("statusBarHeight");
    }
    JsBridge.testAsync = function (params, cb) {
        return this.callJavaAsync("testAsync", params, cb);
    }
    /**
     * 关闭窗口
     * @param {object} params 
     * @param {string} params.name
     */
    JsBridge.closeWin = function (params) {
        return this.callJavaSync("closeWin", params);
    }
    /**
     * 
     * @param {object} params 
     * @param {string} params.name
     * @param {string} params.url
     * @param {object} params.rect
     * @param {number} params.rect.top
     * @param {number} params.rect.left
     * @param {number} params.rect.width
     * @param {number} params.rect.height
     */
    JsBridge.openFrame = function (params) {
        return this.callJavaSync("openFrame", params);
    }
        /**
     * 
     * @param {object} params 
     * @param {string} [params.name]
     */
    JsBridge.closeFrame = function (params) {
        return this.callJavaSync("closeFrame", params);
    }
    /**
     * 
     * @param {Element} ele 
     * @param {object} params 
     * @param {string} params.name
     * @param {string} params.url
     */
    JsBridge.openFrameByElement = function (ele, params) {
        var x = ele.offsetLeft || 0;
        var y = ele.offsetTop || 0;
        var w = ele.offsetWidth || -1;
        var h = ele.offsetHeight || -1;
        params = params || {};
        params.rect = {
            top: y,
            left: x,
            width: w,
            height: h
        };
        return this.callJavaSync("openFrame", params);
    }
    /**
     * 
     * @param {object} params 
     * @param {string} params.name
     * @param {function} params.cb
     */
    JsBridge.addEventListener = function (params, cb) {
        if (!params || !params.name) {
            console.error("params must has a name")
            return
        }
        if (! JsBridge._eventHandlerPool[params.name]) {
            JsBridge._eventHandlerPool[params.name] = [];
        }
        JsBridge._eventHandlerPool[params.name].push(cb)
        return this.callJavaSync("addEventListener", params);
    }
    /**
     * 
     * @param {object} params 
     * @param {string} params.name
     * @param {function} params.cb
     */
    JsBridge.removeEventListener = function (params, cb) {
        if (!params || !params.name) {
            console.error("params must has a name")
            return
        }
        if (!cb) {
            //如果指定了回调方法，只有成功删除了该回调才从java中删除事件监听
            var handlers =  JsBridge._eventHandlerPool[params.name]
            var index;
            if (handlers && (index = handlers.indexOf(cb))) {
                handlers.splice(index, 1);
                if (handlers.length === 0) {
                    return this.callJavaSync("removeEventListener", params);
                } else {
                    return true;
                }
            }
            return false;
        } else {
            //如果没有指定回调方法，默认删除全部
            delete  JsBridge._eventHandlerPool[params.name];
            return this.callJavaSync("removeEventListener", params);
        }
    }
    /**
     * 
     * @param {object} params 
     * @param {string} params.name
     */
    JsBridge.isListening = function (params) {
        return this.callJavaSync("isListening", params);
    }
    /**
     * @Async
     * @param {object} params 
     * @param {string} params.name
     * @param {string} [params.WinName]
     * @param {string} [params.FrameName]
     * @param {object} params.extra
     */
    JsBridge.sendEvent = function (params) {
        return this.callJavaAsync("sendEvent", params);
    }

    /**
     * @Async
     * @param {object} params 
     * @param {string} [params.WinName]
     * @param {string} [params.FrameName]
     * @param {string} params.script
     */
    JsBridge.execScript = function(params) {
        return this.callJavaAsync("execScript", params);
    }
    /**
     * @Async
     * @param {object} params 
     * @param {string} [params.WinName]
     * @param {string} [params.FrameName]
     */
    JsBridge.getPageParam = function(params) {
        return this.callJavaSync("getPageParam", params);
    }
})