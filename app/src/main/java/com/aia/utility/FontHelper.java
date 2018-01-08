package com.aia.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Bynry on 7/11/2017.
 */

public class FontHelper
{

    private static FontHelper mFontHelper = null;
    private static Context mContext = null;

    private static final String FONT_SF_REGULAR = "fonts/sf_ui_text_regular.ttf";
    /*private static final String FONT_KHULA_BOLD = "fonts/Khula-Bold.ttf";
    private static final String FONT_KHULA_EXTRA_BOLD = "fonts/Khula-ExtraBold.ttf";
    private static final String FONT_KHULA_LIGHT = "fonts/Khula-Light.ttf";
    private static final String FONT_KHULA_SEMI_BOLD = "fonts/Khula-SemiBold.ttf";
    private static final String FONT_ALBA_REGULAR = "fonts/Alba-Regular.ttf";*/

    private static Typeface SF_REGULAR;
    /*private static Typeface KHULA_BOLD;
    private static Typeface KHULA_EXTRA_BOLD;
    private static Typeface KHULA_LIGHT;
    private static Typeface KHULA_SEMI_BOLD;
    private static Typeface ALBA_REGULAR;*/

    public enum FontHelperConstant {
        SF_REGULAR,
        /*KHULA_BOLD,
        KHULA_EXTRA_BOLD,
        KHULA_LIGHT,
        KHULA_SEMI_BOLD,
        ALBA_REGULAR*/
    }

    private FontHelper()
    {
        bindFonts();
    }

    public static void applyFont (final Context context, final View root, final String fontPath)
    {
        try
        {
            if (root instanceof ViewGroup)
            {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    applyFont(context, viewGroup.getChildAt(i), fontPath);
            }
            else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param context Context to access assets folder
     *
     * @return FontHelper instance
     */
    public static synchronized FontHelper getInstance (Context context)
    {
        if (mFontHelper == null) {
            mContext = context;
            mFontHelper = new FontHelper();
        }

        return mFontHelper;
    }


    private void bindFonts ()
    {

        SF_REGULAR = Typeface.createFromAsset(mContext.getAssets(), FONT_SF_REGULAR);
        /*KHULA_BOLD = Typeface.createFromAsset(mContext.getAssets(), FONT_KHULA_BOLD);
        KHULA_EXTRA_BOLD = Typeface.createFromAsset(mContext.getAssets(), FONT_KHULA_EXTRA_BOLD);
        KHULA_LIGHT = Typeface.createFromAsset(mContext.getAssets(), FONT_KHULA_LIGHT);
        KHULA_SEMI_BOLD = Typeface.createFromAsset(mContext.getAssets(), FONT_KHULA_SEMI_BOLD);
        ALBA_REGULAR = Typeface.createFromAsset(mContext.getAssets(), FONT_ALBA_REGULAR);*/
    }


    /**
     * @param root View object
     * @param font Font name using FontHelperConstant
     */
    public void applyFont (final View root, FontHelperConstant font)
    {
        Typeface typeface = getFontFromEnum(font);

        if (typeface == null)
            return;

        try
        {
            if (root instanceof ViewGroup)
            {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    applyFont(viewGroup.getChildAt(i), font);
            }
            else if (root instanceof TextView)
                ((TextView) root).setTypeface(typeface);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public Typeface getFontFromEnum (FontHelperConstant fontHelperConstant)
    {
        if (fontHelperConstant == null)
        {
            return null;
        }

        if (fontHelperConstant == FontHelperConstant.SF_REGULAR)
            return SF_REGULAR;

        /*if (fontHelperConstant == FontHelperConstant.KHULA_BOLD)
            return KHULA_BOLD;


        if (fontHelperConstant == FontHelperConstant.KHULA_EXTRA_BOLD)
            return KHULA_EXTRA_BOLD;


        if (fontHelperConstant == FontHelperConstant.KHULA_SEMI_BOLD)
            return KHULA_SEMI_BOLD;


        if (fontHelperConstant == FontHelperConstant.KHULA_LIGHT)
            return KHULA_LIGHT;

        if (fontHelperConstant == FontHelperConstant.ALBA_REGULAR)
            return ALBA_REGULAR;*/

        return null;
    }
}
