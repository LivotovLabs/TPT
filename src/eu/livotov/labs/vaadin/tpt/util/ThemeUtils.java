package eu.livotov.labs.vaadin.tpt.util;

import com.vaadin.service.ApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09.09.12
 */
public class ThemeUtils
{

    private final static Pattern cssImportTrapPattern = Pattern.compile("^@import.*\\\"(.*\\.css)\\\"");

    public static Collection<File> discoverThemesHierarchyOfTheme(final ApplicationContext context, final String theme)
    {
        List<File> themeFolders = new ArrayList<File>();
        File themeFolder = new File(context.getBaseDirectory(), "/VAADIN/themes/" + theme);
        themeFolders.add(themeFolder);
        digThemeFolder(themeFolders, themeFolder);
        return themeFolders;
    }

    private static void digThemeFolder(final List<File> themeFolders, final File themeFolder)
    {
        File stylesFile = new File(themeFolder, "styles.css");

        if (stylesFile.exists())
        {
            Scanner scanner = null;

            try
            {
                scanner = new Scanner(stylesFile, "utf-8");
                while (scanner.hasNext())
                {
                    final String line = scanner.nextLine();
                    if (line != null)
                    {
                        Matcher matcher = cssImportTrapPattern.matcher(line);
                        if (matcher.matches())
                        {
                            File parentThemeFolder = new File(themeFolder, matcher.group(1));
                            themeFolders.add(parentThemeFolder);
                            digThemeFolder(themeFolders,parentThemeFolder);
                            break;
                        }
                    }
                }
            } catch (Throwable err)
            {
                err.printStackTrace();
            } finally
            {
                if (scanner != null)
                {
                    try
                    {
                        scanner.close();
                    } catch (Throwable errnop)
                    {
                    }
                }
            }
        }
    }

}
