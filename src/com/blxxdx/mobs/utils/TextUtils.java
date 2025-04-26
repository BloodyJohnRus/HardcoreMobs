package com.blxxdx.mobs.utils;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class TextUtils {
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }
    public static String applyColor(String message){
        int lastindex = 0;
        while((lastindex = message.indexOf("#", lastindex))<message.length()){
            if(lastindex==-1 && message.lastIndexOf("#")==-1) break;
            message = message.replace(message.substring(lastindex-1, lastindex+7),
                    ChatColor.of(hex2Rgb(message.substring(lastindex, lastindex+7)))+"");
        }
        return message.replace("&",ChatColor.COLOR_CHAR+"");
    }

    private final static int CENTER_PX = 154;

    public static String getCenteredMessage(String message){
        if(message == null || message.isEmpty()) return "";
        message = applyColor(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return (sb + message);
    }
}