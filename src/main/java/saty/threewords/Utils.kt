package saty.threewords;

import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

public class Utils {

    companion object {
        /*fun start(context: Context) {
            context.startActivity(Intent(context, CameraActivity::class.java))
        }*/

        fun isValidSearchText(data: String) : Boolean {
            var regex =  "^/*[^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}$"

            var pattern: Pattern = Pattern.compile(regex)
            var matcher: Matcher = pattern.matcher(data)
            return if (matcher.find()) {
                Log.d("3Word", "$data is the format of a three word address");
                true
            } else {
                Log.d("3Word", "$data is NOT a three word address");
                false
            }
        }
    }

}
