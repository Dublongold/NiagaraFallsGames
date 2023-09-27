package one.two.niagarafallsgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import one.two.niagarafallsgames.R
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.webkit.ValueCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import one.two.niagarafallsgames.help.*
import java.io.File
import java.io.IOException
import java.net.URL

class WebViewPage: Fragment() {
    private lateinit var fragmentWebView: WebView
    private var fileWayCallback: ValueCallback<Array<Uri>>? = null
    private var wayCallback: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_page_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onViewCreated(view, savedInstanceState)
        fragmentWebView = view.findViewById(R.id.fragmentWebView)
        setSettingsSettings()
        setClients()
        if(fragmentWebView.settings.run {
            allowFileAccess == domStorageEnabled
            }) {
            val url = URL(Firebase.remoteConfig.getString("url"))
            fragmentWebView.loadUrl(url.toString())
        }
    }

    private fun setSettingsSettings() {
        HelpWithFragmentWebView1(fragmentWebView.settings)[true]
        HelpWithFragmentWebView2(fragmentWebView.settings)[true]
        HelpWithFragmentWebView3(fragmentWebView.settings, fragmentWebView)[true]
        HelpWithFragmentWebView4(fragmentWebView.settings)[true]
    }

    private fun setClients() {
        val clientSetter = ClientSetter(fragmentWebView)
        clientSetter.requestPermissionLauncher = requestPermissionLauncher
        fun setFileWayCallback(value: ValueCallback<Array<Uri>>?) {
            fileWayCallback = value
        }
        clientSetter.setFileWayCallback = ::setFileWayCallback
        clientSetter.setWebChromeClient()
        clientSetter.setWebViewClient()
    }

    val requestPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean? ->

        var requestCode = 0

        val grandIntent = IntentBuilder()
            .action(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .type("*/*")
            .build()
        requestCode += 1

        val imgFile: File? = try {
            requireActivity().createTempImageFile("img")
        } catch (ex: IOException) {
            Log.e("Img file", "Error...", ex)
            null
        }

        requestCode *= 5
        val gimmeImage = IntentBuilder()
            .action(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile))
            .build()
        wayCallback = Uri.fromFile(imgFile)

        val choChooo = IntentBuilder()
            .action(Intent.ACTION_CHOOSER)
            .putExtra(Intent.EXTRA_INTENT, grandIntent)
            .putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(gimmeImage))
            .build()

        requestCode -= 4
        startActivityForResult(choChooo, requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        val fileWayCallbackObj = FileWayCallback(fileWayCallback)
        if (resultCode == resultCodeForFileWayCallback) {
            val u = arrayOf(Uri.parse(data?.dataString))
            if (data != defaultFileWayCallbackResult
                && data.dataString != defaultFileWayCallbackResult) {
                fileWayCallbackObj.onReceiveValue(u)
            } else {
                fileWayCallbackObj.default(wayCallback)
            }
        } else {
            fileWayCallbackObj.onReceiveValue(defaultFileWayCallbackResult)
        }
        fileWayCallback = defaultFileWayCallbackResult
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragmentWebView.saveState(outState)
    }

    private val defaultFileWayCallbackResult = null
    private val resultCodeForFileWayCallback = -1
}