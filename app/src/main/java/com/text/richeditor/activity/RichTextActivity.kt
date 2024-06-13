package com.text.richeditor.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.activity_rich_text.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.text.richeditor.R
import com.text.richeditor.dialog.CommonPopupWindow
import com.text.richeditor.dialog.InputLinkDialog
import com.text.richeditor.editor.ActionType
import com.text.richeditor.editor.fragment.EditorMenuFragment
import com.text.richeditor.editor.interfaces.OnActionPerformListener
import com.text.richeditor.utils.KeyBoardUtils
import com.text.richeditor.utils.RichUtils


class RichTextActivity :AppCompatActivity(), OnActionPerformListener {


    private var popupWindow //编辑图片的pop
            : CommonPopupWindow? = null
    private var currentUrl=""

    private val mEditorMenuFragment by lazy{ EditorMenuFragment(R.id.fl_action) }
    private var isKeyboardShowing = false

    private val inputLinkDialog by lazy { InputLinkDialog(this, sureListener = {href, name ->
        againEdit()
        rich_Editor.insertLink(href,name)
    }) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rich_text)
        initData()

    }

    private fun initData(){
        initPop()
        initEditor()
    }
    private fun initEditor() {
        //输入框显示字体的大小
        rich_Editor.setEditorFontSize(16)
        //输入框显示字体的颜色
        rich_Editor.setEditorFontColor(ContextCompat.getColor(this,R.color.deepGrey))
        //输入框背景设置
        rich_Editor.setEditorBackgroundColor(Color.WHITE)
        //输入框文本padding
        rich_Editor.setPadding(10, 10, 10, 10)
        //输入提示文本
        rich_Editor.setPlaceholder("点击输入正文！~")

        //文本输入框监听事件
        rich_Editor.setOnTextChangeListener { text -> Log.e("富文本文字变动", text!!) }
        edit_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                tv_sum.text = edit_name.text.toString().length.toString()+"/100"
                val html = rich_Editor.html
            }
        })
        rich_Editor.setOnDecorationChangeListener { _, types ->
            val flagArr = ArrayList<String>()
            for (i in types.indices) {
                flagArr.add(types[i].name)
            }
            if (flagArr.contains("BOLD")) {
                button_bold.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.BOLD, true)
            } else {
                button_bold.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.BOLD, false)
            }
            if (flagArr.contains("ITALIC")) {
                button_italic.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.ITALIC, true)
            } else {
                button_italic.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.ITALIC, false)
            }
            if (flagArr.contains("STRIKETHROUGH")) {
                button_strikethrough.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.STRIKETHROUGH, true)
            } else {
                button_strikethrough.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.STRIKETHROUGH, false)
            }
            if (flagArr.contains("JUSTIFYCENTER")) {
                button_align_center.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                button_align_left.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                button_align_right.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_CENTER, true)
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_LEFT, false)
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_RIGHT, false)
            } else {
                button_align_center.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_CENTER, false)
            }
            if (flagArr.contains("JUSTIFYLEFT")) {
                button_align_center.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                button_align_left.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                button_align_right.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_CENTER, false)
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_LEFT, true)
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_RIGHT, false)
            } else {
                button_align_left.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_LEFT, false)
            }
            if (flagArr.contains("JUSTIFYRIGHT")) {
                button_align_center.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                button_align_left.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                button_align_right.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_CENTER, false)
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_LEFT, false)
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_RIGHT, true)
            } else {
                button_align_right.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.JUSTIFY_RIGHT, false)
            }
            if (flagArr.contains("UNDERLINE")) {
                button_underline.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.UNDERLINE, true)
            } else {
                button_underline.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.UNDERLINE, false)
            }
            if (flagArr.contains("ORDEREDLIST")) {
                button_list_ol.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                button_list_ul.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.ORDERED, true)
                mEditorMenuFragment.updateActionStates(ActionType.UNORDERED, false)
            } else {
                button_list_ol.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.ORDERED, false)
            }
            if (flagArr.contains("UNORDEREDLIST")) {
                button_list_ul.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.colorPrimary
                    )
                )
                button_list_ol.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.ORDERED, false)
                mEditorMenuFragment.updateActionStates(ActionType.UNORDERED, true)
            } else {
                button_list_ul.setColorFilter(
                    ContextCompat.getColor(
                        this@RichTextActivity,
                        R.color.tintColor
                    )
                )
                mEditorMenuFragment.updateActionStates(ActionType.UNORDERED, false)
            }
        }
        rich_Editor.setImageClickListener { imageUrl ->
            currentUrl = imageUrl
            popupWindow?.showBottom(ll_container, 0.5f)
        }

        button_rich_do.setOnClickListener { rich_Editor.redo() }
        button_rich_undo.setOnClickListener { rich_Editor.undo()}
        button_bold.setOnClickListener {
            againEdit()
            rich_Editor.setBold() }
        button_underline.setOnClickListener {
            againEdit()
            rich_Editor.setUnderline()
        }
        button_italic.setOnClickListener {
            againEdit()
            rich_Editor.setItalic()
        }
        button_strikethrough.setOnClickListener {
            againEdit()
            rich_Editor.setStrikeThrough()
        }
        //字号
        button_h1.setOnClickListener {
            againEdit()
            rich_Editor.setFontSize(2)
        }
        button_h2.setOnClickListener {
            againEdit()
            rich_Editor.setFontSize(3)
        }
        button_h3.setOnClickListener {
            againEdit()
            rich_Editor.setFontSize(4)
        }
        button_h4.setOnClickListener {
            againEdit()
            rich_Editor.setFontSize(5)
        }
        button_indent_decrease.setOnClickListener {
            againEdit()
            rich_Editor.setOutdent()
        }
        button_indent_increase.setOnClickListener {
            againEdit()
            rich_Editor.setIndent()
        }
        button_list_ul.setOnClickListener {
            againEdit()
            rich_Editor.setBullets()
        }
        button_list_ol.setOnClickListener {
            againEdit()
            rich_Editor.setNumbers()
        }
        button_align_center.setOnClickListener {
            againEdit()
            rich_Editor.setAlignCenter()
        }
        button_align_left.setOnClickListener {
            againEdit()
            rich_Editor.setAlignLeft()
        }
        button_align_right.setOnClickListener {
            againEdit()
            rich_Editor.setAlignRight()
        }
        button_link.setOnClickListener {
            KeyboardUtils.hideSoftInput(this@RichTextActivity)
            inputLinkDialog.show()
        }
        //选择图片
        button_image.setOnClickListener {
            selectImage()
        }
        //选择视频
        button_video.setOnClickListener {
            selectVideo()
        }

        mEditorMenuFragment.setActionClickListener(this)
        supportFragmentManager.beginTransaction().add(R.id.fl_action, mEditorMenuFragment, EditorMenuFragment::class.java.name).commit()
        KeyboardUtils.registerSoftInputChangedListener(this) { height: Int ->
            //键盘打开
            isKeyboardShowing = height > 0
            if (height > 0) {
                //fl_action.visibility = View.GONE
                val params = fl_action.layoutParams
                params.height = height/2
                fl_action.layoutParams = params
            } else{
                //if (fl_action.visibility != View.VISIBLE)
                fl_action.visibility = View.GONE
                iv_action.setColorFilter(ContextCompat.getColor(this@RichTextActivity, R.color.tintColor))
            }
        }

        iv_action.setOnClickListener {
            if (fl_action.visibility == View.VISIBLE) {
                fl_action.visibility = View.GONE
                iv_action.setColorFilter(ContextCompat.getColor(this@RichTextActivity, R.color.tintColor))
            } else {
//                if (isKeyboardShowing) {
//                    KeyboardUtils.hideSoftInput(this@RichTextActivity)
//                }
                KeyboardUtils.showSoftInput(this@RichTextActivity)
                fl_action.visibility = View.VISIBLE
                iv_action.setColorFilter(ContextCompat.getColor(this@RichTextActivity, R.color.colorPrimary))
            }
        }
    }


    private fun initPop() {
        val view= LayoutInflater.from(this@RichTextActivity).inflate(R.layout.pop_picture, null)
        view.findViewById<View>(R.id.linear_cancle)
            .setOnClickListener { popupWindow?.dismiss() }
        view.findViewById<View>(R.id.linear_delete_pic).setOnClickListener { v: View? ->
            //删除图片
            val removeUrl =
                "<img src=\"" + currentUrl + "\" alt=\"delImg\" width=\"80" +
                        "%\"><br>"
            val newUrl: String = rich_Editor.html.replace(removeUrl, "")
            currentUrl = ""
            rich_Editor.html = newUrl
            if (RichUtils.isEmpty(rich_Editor.html)) {
                rich_Editor.html = ""
            }
            popupWindow?.dismiss()
        }
        popupWindow = CommonPopupWindow.Builder(this@RichTextActivity)
            .setView(view)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true) //在外不可用手指取消
            .setAnimationStyle(R.style.pop_animation) //设置popWindow的出场动画
            .create()
        popupWindow?.setOnDismissListener(PopupWindow.OnDismissListener {
            rich_Editor.setInputEnabled(
                true
            )
        })
    }

    private fun againEdit() {
        //如果第一次点击例如加粗，没有焦点时，获取焦点并弹出软键盘
        rich_Editor.focusEditor()
        KeyBoardUtils.openKeyboard(edit_name, this@RichTextActivity)
    }
    

    override fun onActionPerform(type: ActionType?, vararg values: Any?) {
        var value = ""
        if (values != null && values.isNotEmpty()) {
            value = values[0] as String
        }

        againEdit()
        when (type) {
            ActionType.FORE_COLOR -> { rich_Editor.setTextColor(value)}
            ActionType.BOLD -> { rich_Editor.setBold()}
            ActionType.ITALIC -> rich_Editor.setItalic()
            ActionType.UNDERLINE -> rich_Editor.setUnderline()
            ActionType.STRIKETHROUGH -> rich_Editor.setStrikeThrough()
            ActionType.H1 -> {
                rich_Editor.setFontSize(2)
                mEditorMenuFragment.updateActionStates(ActionType.H1,true)
                mEditorMenuFragment.updateActionStates(ActionType.H2,false)
                mEditorMenuFragment.updateActionStates(ActionType.H3,false)
                mEditorMenuFragment.updateActionStates(ActionType.H4,false)
            }
            ActionType.H2 -> {
                rich_Editor.setFontSize(3)
                mEditorMenuFragment.updateActionStates(ActionType.H1,false)
                mEditorMenuFragment.updateActionStates(ActionType.H2,true)
                mEditorMenuFragment.updateActionStates(ActionType.H3,false)
                mEditorMenuFragment.updateActionStates(ActionType.H4,false)
            }
            ActionType.H3 -> {
                rich_Editor.setFontSize(4)
                mEditorMenuFragment.updateActionStates(ActionType.H1,false)
                mEditorMenuFragment.updateActionStates(ActionType.H2,false)
                mEditorMenuFragment.updateActionStates(ActionType.H3,true)
                mEditorMenuFragment.updateActionStates(ActionType.H4,false)
            }
            ActionType.H4 -> {
                rich_Editor.setFontSize(5)
                mEditorMenuFragment.updateActionStates(ActionType.H1,false)
                mEditorMenuFragment.updateActionStates(ActionType.H2,false)
                mEditorMenuFragment.updateActionStates(ActionType.H3,false)
                mEditorMenuFragment.updateActionStates(ActionType.H4,true)
            }
            ActionType.JUSTIFY_LEFT -> rich_Editor.setAlignLeft()
            ActionType.JUSTIFY_CENTER -> rich_Editor.setAlignCenter()
            ActionType.JUSTIFY_RIGHT -> rich_Editor.setAlignRight()
            ActionType.INDENT -> rich_Editor.setIndent()
            ActionType.OUTDENT -> rich_Editor.setOutdent()
            ActionType.UNORDERED->rich_Editor.setBullets()
            ActionType.ORDERED->rich_Editor.setNumbers()
            ActionType.IMAGE -> {
                selectImage()
            }
            ActionType.LINK -> {
                KeyboardUtils.hideSoftInput(this@RichTextActivity)
                inputLinkDialog.show()
            }
            ActionType.VIDEO -> {
                selectVideo()
            }
            else -> {}
        }
    }


    fun selectImage(){
        // 进入相册

    }

    fun selectVideo(){
        // 选择视频 以下是例子：不需要的api可以不写

    }
}