package github.hotstu.maotai.engine;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

import github.hotstu.labo.jsbridge.core.WebChromeClientDelegate;
import github.hotstu.naiue.dialog.MODialog;
import github.hotstu.naiue.dialog.MODialogAction;

/**
 * @author hglf
 * @since 2018/8/3
 */
public class MDWebChromeClient extends WebChromeClientDelegate {
    private WebChromeClient positiveDelegate;
    public MDWebChromeClient(WebChromeClient delegate) {
        super(delegate);
        this.positiveDelegate = delegate;
    }


    /**
     * Tell the client to display a javascript alert dialog.  If the client
     * returns true, WebView will assume that the client will handle the
     * dialog.  If the client returns false, it will continue execution.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult to confirm that the user hit enter.
     * @return boolean Whether the client will handle the alert dialog.
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if(positiveDelegate!= null && positiveDelegate.onJsAlert( view,  url,  message,  result)) {
            return true;
        } else {
            new MODialog.MessageDialogBuilder(view.getContext())
                    .setMessage(message)
                    .addAction("确定", (dialog, index) -> {
                        dialog.dismiss();
                        result.confirm();
                    })
                    .show()
                    .setOnCancelListener(dialog -> {
                        result.cancel();
                    });
            return true;
        }

    }

    /**
     * Tell the client to display a confirm dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * confirm dialog and call the appropriate JsResult method. If the
     * client returns false, a default value of false will be returned to
     * javascript. The default behavior is to return false.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult used to send the user's response to
     *                javascript.
     * @return boolean Whether the client will handle the confirm dialog.
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if(positiveDelegate!= null && positiveDelegate.onJsConfirm( view,  url,  message,  result)) {
            return true;
        } else {
            new MODialog.MessageDialogBuilder(view.getContext())
                    .setMessage(message)
                    .addAction(0,"取消", MODialogAction.ACTION_PROP_POSITIVE,(dialog, index) -> {
                        dialog.dismiss();
                        result.cancel();
                    })
                    .addAction(0,"确定", MODialogAction.ACTION_PROP_NEGATIVE,(dialog, index) -> {
                        dialog.dismiss();
                        result.confirm();
                    })
                    .show()
                    .setOnCancelListener(dialog -> {
                        result.cancel();
                    });
            return true;
        }

    }

    /**
     * Tell the client to display a prompt dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * prompt dialog and call the appropriate JsPromptResult method. If the
     * client returns false, a default value of false will be returned to to
     * javascript. The default behavior is to return false.
     *
     * @param view         The WebView that initiated the callback.
     * @param url          The url of the page requesting the dialog.
     * @param message      Message to be displayed in the window.
     * @param defaultValue The default value displayed in the prompt dialog.
     * @param result       A JsPromptResult used to send the user's reponse to
     *                     javascript.
     * @return boolean Whether the client will handle the prompt dialog.
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if(positiveDelegate!= null && positiveDelegate.onJsPrompt( view,  url,  message, defaultValue, result)) {
            return true;
        } else {
            MODialog.EditTextDialogBuilder builder = new MODialog.EditTextDialogBuilder(view.getContext());
            builder.setTitle(message);
            builder.setPlaceholder("请输入");
            final EditText editText = builder.getEditText();
            editText.setText(defaultValue);
            builder.addAction("确定", (dialog, index) -> {
                dialog.dismiss();
                result.confirm(editText.getText().toString());
            });
            builder.show().setOnCancelListener(dialog -> {
                result.cancel();
            });
            return true;
        }


    }
}
