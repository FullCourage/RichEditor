package com.text.richeditor.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import com.text.richeditor.R
import kotlinx.android.synthetic.main.input_link_dialog.*

class InputLinkDialog(val activity: Activity,val sureListener:(href:String,name:String)->Unit) : Dialog(activity, R.style.up_and_down_dialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_link_dialog)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tv_cancel.setOnClickListener {
            et_href.text.clear()
            et_name.text.clear()
            this.dismiss()
        }
        tv_sure.setOnClickListener {
            if(et_href.text.trim().isEmpty()){
                //地址不能为空
            }else{
                sureListener(et_href.text.trim().toString(),
                    if(et_name.text.trim().isEmpty()) et_href.text.trim().toString() else et_name.text.trim().toString())
                et_href.text.clear()
                et_name.text.clear()
                dismiss()
            }
        }
    }
}