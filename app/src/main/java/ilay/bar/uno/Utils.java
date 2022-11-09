package ilay.bar.uno;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// class is abstract since it only contains static methods
public abstract class Utils
{
    public static final String imgDir = "/images";


    public static int string2drawbleId(Context context, String drawableName)
    {
        int drawableId =
                context.getResources().getIdentifier(
                        drawableName, "drawable",
                        context.getPackageName());
        return drawableId;
    }

    public static Bitmap stringDrawableBitmap(Context context, String drawableName)
    {
        int id = string2drawbleId(context, drawableName);
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
        return bmp;
    }


}
