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
 *
 * Provides set of utility methods for dealing with Vaadin themes. Basically this class is used internally by
 * TPT but please feel free to use all public methods in your application if you need them.
 */
public class ThemeUtils
{

    /**
     * RegEx pattern to detect a css @import statement in a single line of text and extract the referenced css from it.
     */
    private final static Pattern cssImportTrapPattern = Pattern.compile("^@import.*\\\"(.*\\.css)\\\"");

    /**
     * Provides list of specified theme folder and all parent (imported) themes by reading theme css file and
     * digging into the parent theme by following css @import statement. Note, that only first import statement
     * is followed, as proper Vaadin theme may contain only one import of parent theme and it must be the first
     * import statement in a file.
     * @param context Vaadin app context
     * @param theme theme name to dig folders for
     * @return collection of this theme and parent theme(s) folders. The specified theme folder comes first in this
     * collection.
     */
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
                            File parentThemeFolder = new File(themeFolder, matcher.group(1)).getParentFile();
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
