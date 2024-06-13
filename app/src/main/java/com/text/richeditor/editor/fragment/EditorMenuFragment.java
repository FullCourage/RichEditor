package com.text.richeditor.editor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.text.richeditor.R;
import com.text.richeditor.editor.ActionType;
import com.text.richeditor.editor.ColorPaletteView;
import com.text.richeditor.editor.interfaces.OnActionPerformListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class EditorMenuFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    ColorPaletteView cpvFontTextColor;
    int viewIds;
    public EditorMenuFragment(){}
    public EditorMenuFragment(int viewIds){
        this.viewIds=viewIds;
    }

    private OnActionPerformListener mActionClickListener;

    private final static Pattern PATTERN_RGB =
            Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");

    private Map<Integer, ActionType> mViewTypeMap = new HashMap<Integer, ActionType>() {
        {
            put(R.id.iv_action_bold, ActionType.BOLD);
            put(R.id.iv_action_italic, ActionType.ITALIC);
            put(R.id.iv_action_underline, ActionType.UNDERLINE);
            put(R.id.iv_action_strikethrough, ActionType.STRIKETHROUGH);
            put(R.id.iv_action_justify_left, ActionType.JUSTIFY_LEFT);
            put(R.id.iv_action_justify_center, ActionType.JUSTIFY_CENTER);
            put(R.id.iv_action_justify_right, ActionType.JUSTIFY_RIGHT);
            put(R.id.iv_action_insert_numbers, ActionType.ORDERED);
            put(R.id.iv_action_insert_bullets, ActionType.UNORDERED);
            put(R.id.iv_action_indent, ActionType.INDENT);
            put(R.id.iv_action_outdent, ActionType.OUTDENT);
            put(R.id.ll_h1, ActionType.H1);
            put(R.id.ll_h2, ActionType.H2);
            put(R.id.ll_h3, ActionType.H3);
            put(R.id.ll_h4, ActionType.H4);
            put(R.id.iv_action_insert_image, ActionType.IMAGE);
            put(R.id.iv_action_insert_link, ActionType.LINK);
            put(R.id.iv_action_insert_video, ActionType.VIDEO);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.editor_menu_fragment, null);
        //ButterKnife.bind(this, rootView);
        cpvFontTextColor=rootView.findViewById(R.id.cpv_font_text_color);

        rootView.findViewById(R.id.iv_action_bold).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_italic).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_underline).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_strikethrough).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_justify_left).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_justify_center).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_insert_numbers).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_insert_bullets).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_indent).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_outdent).setOnClickListener(this);
        rootView.findViewById(R.id.ll_h1).setOnClickListener(this);
        rootView.findViewById(R.id.ll_h2).setOnClickListener(this);
        rootView.findViewById(R.id.ll_h3).setOnClickListener(this);
        rootView.findViewById(R.id.ll_h4).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_outdent).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_insert_image).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_insert_link).setOnClickListener(this);
        rootView.findViewById(R.id.iv_action_insert_video).setOnClickListener(this);

        initView();
        return rootView;
    }

    private void initView() {
        cpvFontTextColor.setOnColorChangeListener(color -> {
            if (mActionClickListener != null) {
                mActionClickListener.onActionPerform(ActionType.FORE_COLOR, color);
            }
        });
    }



    @Override
    public void onClick(View v) {
        if (mActionClickListener == null) {
            return;
        }
        ActionType type = mViewTypeMap.get(v.getId());
        mActionClickListener.onActionPerform(type);
    }

    //通过参数实时更新菜单颜色
    public void updateActionStates(final ActionType type, final boolean isActive) {
        rootView.post(() -> {
            View view = null;
            for (Map.Entry<Integer, ActionType> e : mViewTypeMap.entrySet()) {
                Integer key = e.getKey();
                if (e.getValue() == type) {
                    view = rootView.findViewById(key);
                    break;
                }
            }

            if (view == null) {
                return;
            }

            switch (type) {
                case BOLD:
                case ITALIC:
                case UNDERLINE:
                case SUBSCRIPT:
                case SUPERSCRIPT:
                case STRIKETHROUGH:
                case JUSTIFY_LEFT:
                case JUSTIFY_CENTER:
                case JUSTIFY_RIGHT:
                case JUSTIFY_FULL:
                case ORDERED:
                case CODE_VIEW:
                case UNORDERED:
                    if (isActive) {
                        ((ImageView) view).setColorFilter(
                                ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                    } else {
                        ((ImageView) view).setColorFilter(
                                ContextCompat.getColor(requireContext(), R.color.tintColor));
                    }
                    break;
                case H1:
                case H2:
                case H3:
                case H4:
                    if (isActive) {
                        view.setBackgroundResource(R.drawable.round_rectangle_blue);
                    } else {
                        view.setBackgroundResource(R.drawable.round_rectangle_white);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    public void setActionClickListener(OnActionPerformListener mActionClickListener) {
        this.mActionClickListener = mActionClickListener;
    }

}
