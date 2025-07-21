package me.theparanker.duel.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;

@UtilityClass
public final class CC {
    public static final String PRIMARY = ChatColor.LIGHT_PURPLE.toString();
    public static final String SECONDARY = ChatColor.GRAY.toString();
    public static final String BLUE = ChatColor.BLUE.toString();
    public static final String AQUA = ChatColor.AQUA.toString();
    public static final String YELLOW = ChatColor.YELLOW.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String GRAY = ChatColor.GRAY.toString();
    public static final String GOLD = ChatColor.GOLD.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    public static final String WHITE = ChatColor.WHITE.toString();
    public static final String BLACK = ChatColor.BLACK.toString();
    public static final String BOLD = ChatColor.BOLD.toString();
    public static final String ITALIC = ChatColor.ITALIC.toString();
    public static final String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
    public static final String RESET = ChatColor.RESET.toString();
    public static final String MAGIC;
    public static final String OBFUSCATED;
    public static final String B;
    public static final String M;
    public static final String O;
    public static final String I;
    public static final String S;
    public static final String R;
    public static final String DARK_BLUE;
    public static final String DARK_AQUA;
    public static final String DARK_GRAY;
    public static final String DARK_GREEN;
    public static final String DARK_PURPLE;
    public static final String DARK_RED;
    public static final String D_BLUE;
    public static final String D_AQUA;
    public static final String D_GRAY;
    public static final String D_GREEN;
    public static final String D_PURPLE;
    public static final String D_RED;
    public static final String LIGHT_PURPLE;
    public static final String L_PURPLE;
    public static final String PINK;
    public static final String B_BLUE;
    public static final String B_AQUA;
    public static final String B_YELLOW;
    public static final String B_RED;
    public static final String B_GRAY;
    public static final String B_GOLD;
    public static final String B_GREEN;
    public static final String B_WHITE;
    public static final String B_BLACK;
    public static final String BD_BLUE;
    public static final String BD_AQUA;
    public static final String BD_GRAY;
    public static final String BD_GREEN;
    public static final String BD_PURPLE;
    public static final String BD_RED;
    public static final String BL_PURPLE;
    public static final String I_BLUE;
    public static final String I_AQUA;
    public static final String I_YELLOW;
    public static final String I_RED;
    public static final String I_GRAY;
    public static final String I_GOLD;
    public static final String I_GREEN;
    public static final String I_WHITE;
    public static final String I_BLACK;
    public static final String ID_RED;
    public static final String ID_BLUE;
    public static final String ID_AQUA;
    public static final String ID_GRAY;
    public static final String ID_GREEN;
    public static final String ID_PURPLE;
    public static final String IL_PURPLE;
    public static final String VAPE = "§8 §8 §1 §3 §3 §7 §8 §r";
    public static final String BLANK_LINE = "§8 §8 §1 §3 §3 §7 §8 §r";
    public static final String BL = "§8 §8 §1 §3 §3 §7 §8 §r";
    public static final String SB_BAR;
    public static final String MENU_BAR;
    public static final String DARK_MENU_BAR;
    public static final String CHAT_BAR;
    public static final String onlyPlayer;

    public static List<String> translateStrings(List<String> untranslated) {
        return untranslated.stream().map(CC::translate).toList();
    }

    public static List<String> translate(List<String> untranslated) {
        return CC.translateStrings(untranslated);
    }

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)input);
    }


    static {
        OBFUSCATED = MAGIC = ChatColor.MAGIC.toString();
        B = BOLD;
        M = MAGIC;
        O = MAGIC;
        I = ITALIC;
        S = STRIKE_THROUGH;
        R = RESET;
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        D_BLUE = DARK_BLUE;
        D_AQUA = DARK_AQUA;
        D_GRAY = DARK_GRAY;
        D_GREEN = DARK_GREEN;
        D_PURPLE = DARK_PURPLE;
        D_RED = DARK_RED;
        PINK = L_PURPLE = (LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString());
        B_BLUE = BLUE + B;
        B_AQUA = AQUA + B;
        B_YELLOW = YELLOW + B;
        B_RED = RED + B;
        B_GRAY = GRAY + B;
        B_GOLD = GOLD + B;
        B_GREEN = GREEN + B;
        B_WHITE = WHITE + B;
        B_BLACK = BLACK + B;
        BD_BLUE = D_BLUE + B;
        BD_AQUA = D_AQUA + B;
        BD_GRAY = D_GRAY + B;
        BD_GREEN = D_GREEN + B;
        BD_PURPLE = D_PURPLE + B;
        BD_RED = D_RED + B;
        BL_PURPLE = L_PURPLE + B;
        I_BLUE = BLUE + I;
        I_AQUA = AQUA + I;
        I_YELLOW = YELLOW + I;
        I_RED = RED + I;
        I_GRAY = GRAY + I;
        I_GOLD = GOLD + I;
        I_GREEN = GREEN + I;
        I_WHITE = WHITE + I;
        I_BLACK = BLACK + I;
        ID_RED = D_RED + I;
        ID_BLUE = D_BLUE + I;
        ID_AQUA = D_AQUA + I;
        ID_GRAY = D_GRAY + I;
        ID_GREEN = D_GREEN + I;
        ID_PURPLE = D_PURPLE + I;
        IL_PURPLE = L_PURPLE + I;
        SB_BAR = ChatColor.GRAY.toString() + String.valueOf(ChatColor.STRIKETHROUGH) + "----------------------";
        MENU_BAR = ChatColor.GRAY.toString() + String.valueOf(ChatColor.STRIKETHROUGH) + "----------------------------";
        DARK_MENU_BAR = ChatColor.DARK_GRAY.toString() + String.valueOf(ChatColor.STRIKETHROUGH) + "--------------------------------";
        CHAT_BAR = ChatColor.GRAY.toString() + String.valueOf(ChatColor.STRIKETHROUGH) + "------------------------------------------------";
        onlyPlayer = CC.translate("&cSolo i giocatori possono eseguire questo comando.");
    }
}
