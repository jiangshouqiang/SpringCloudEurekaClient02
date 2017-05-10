/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gr.jiang.spring.cloud.config;

import java.beans.Introspector;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.WeakHashMap;

/**
 * Utility class to deal with Java language related issues, such as type conversions.
 *
 * @author Glen Daniels (gdaniels@apache.org)
 */
public class JavaUtils {
    private JavaUtils() {
    }

    // protected static Log log =
    // LogFactory.getLog(JavaUtils.class.getName());

    public static final char NL = '\n';

    public static final char CR = '\r';

    /**
     * The prefered line separator
     */
    public static final String LS = System.getProperty("line.separator",
            (new Character(NL)).toString());

    public static Class getWrapperClass(Class primitive) {
        if (primitive == int.class)
            return Integer.class;
        else if (primitive == short.class)
            return Short.class;
        else if (primitive == boolean.class)
            return Boolean.class;
        else if (primitive == byte.class)
            return Byte.class;
        else if (primitive == long.class)
            return Long.class;
        else if (primitive == double.class)
            return Double.class;
        else if (primitive == float.class)
            return Float.class;
        else if (primitive == char.class)
            return Character.class;

        return null;
    }

    public static String getWrapper(String primitive) {
        if (primitive.equals("int"))
            return "Integer";
        else if (primitive.equals("short"))
            return "Short";
        else if (primitive.equals("boolean"))
            return "Boolean";
        else if (primitive.equals("byte"))
            return "Byte";
        else if (primitive.equals("long"))
            return "Long";
        else if (primitive.equals("double"))
            return "Double";
        else if (primitive.equals("float"))
            return "Float";
        else if (primitive.equals("char"))
            return "Character";

        return null;
    }

    public static Class getPrimitiveClass(Class wrapper) {
        if (wrapper == Integer.class)
            return int.class;
        else if (wrapper == Short.class)
            return short.class;
        else if (wrapper == Boolean.class)
            return boolean.class;
        else if (wrapper == Byte.class)
            return byte.class;
        else if (wrapper == Long.class)
            return long.class;
        else if (wrapper == Double.class)
            return double.class;
        else if (wrapper == Float.class)
            return float.class;
        else if (wrapper == Character.class)
            return char.class;

        return null;
    }

    public static Class getPrimitiveClassFromName(String primitive) {
        if (primitive.equals("int"))
            return int.class;
        else if (primitive.equals("short"))
            return short.class;
        else if (primitive.equals("boolean"))
            return boolean.class;
        else if (primitive.equals("byte"))
            return byte.class;
        else if (primitive.equals("long"))
            return long.class;
        else if (primitive.equals("double"))
            return double.class;
        else if (primitive.equals("float"))
            return float.class;
        else if (primitive.equals("char"))
            return char.class;

        return null;
    }

    /**
     * It the argument to the convert(...) method implements the ConvertCache interface, the convert(...) method will
     * use the set/get methods to store and retrieve converted values.
     **/
    public interface ConvertCache {
        /**
         * Set/Get converted values of the convert method.
         **/
        void setConvertedValue(Class cls, Object value);

        Object getConvertedValue(Class cls);

        /**
         * Get the destination array class described by the mxml
         **/
        Class getDestClass();
    }

    /**
     * These are java keywords as specified at the following URL (sorted alphabetically).
     * http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#229308 Note that false, true, and null
     * are not strictly keywords; they are literal values, but for the purposes of this array, they can be treated as
     * literals. ****** PLEASE KEEP THIS LIST SORTED IN ASCENDING ORDER ******
     */
    static final String keywords[] = {"abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double", "else",
            "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
            "volatile", "while"};

    /**
     * Collator for comparing the strings
     */
    static final Collator englishCollator = Collator.getInstance(Locale.ENGLISH);

    /**
     * Use this character as suffix
     */
    static final char keywordPrefix = '_';

    /**
     * isJavaId Returns true if the name is a valid java identifier.
     *
     * @param id to check
     * @return boolean true/false
     **/
    public static boolean isJavaId(String id) {
        if (id == null || id.equals("") || isJavaKeyword(id))
            return false;
        if (!Character.isJavaIdentifierStart(id.charAt(0)))
            return false;
        for (int i = 1; i < id.length(); i++)
            if (!Character.isJavaIdentifierPart(id.charAt(i)))
                return false;
        return true;
    }

    /**
     * checks if the input string is a valid java keyword.
     *
     * @return boolean true/false
     */
    public static boolean isJavaKeyword(String keyword) {
        return (Arrays.binarySearch(keywords, keyword, englishCollator) >= 0);
    }

    /**
     * Turn a java keyword string into a non-Java keyword string. (Right now this simply means appending an underscore.)
     */
    public static String makeNonJavaKeyword(String keyword) {
        return keywordPrefix + keyword;
    }

    /**
     * Converts text of the form Foo[] to the proper class name for loading [LFoo
     */
    public static String getLoadableClassName(String text) {
        if (text == null || text.indexOf("[") < 0 || text.charAt(0) == '[')
            return text;
        String className = text.substring(0, text.indexOf("["));
        if (className.equals("byte"))
            className = "B";
        else if (className.equals("char"))
            className = "C";
        else if (className.equals("double"))
            className = "D";
        else if (className.equals("float"))
            className = "F";
        else if (className.equals("int"))
            className = "I";
        else if (className.equals("long"))
            className = "J";
        else if (className.equals("short"))
            className = "S";
        else if (className.equals("boolean"))
            className = "Z";
        else
            className = "L" + className + ";";
        int i = text.indexOf("]");
        while (i > 0) {
            className = "[" + className;
            i = text.indexOf("]", i + 1);
        }
        return className;
    }

    /**
     * Converts text of the form [LFoo to the Foo[]
     */
    public static String getTextClassName(String text) {
        if (text == null || text.indexOf("[") != 0)
            return text;
        String className = "";
        int index = 0;
        while (index < text.length() && text.charAt(index) == '[') {
            index++;
            className += "[]";
        }
        if (index < text.length()) {
            if (text.charAt(index) == 'B')
                className = "byte" + className;
            else if (text.charAt(index) == 'C')
                className = "char" + className;
            else if (text.charAt(index) == 'D')
                className = "double" + className;
            else if (text.charAt(index) == 'F')
                className = "float" + className;
            else if (text.charAt(index) == 'I')
                className = "int" + className;
            else if (text.charAt(index) == 'J')
                className = "long" + className;
            else if (text.charAt(index) == 'S')
                className = "short" + className;
            else if (text.charAt(index) == 'Z')
                className = "boolean" + className;
            else {
                className = text.substring(index + 1, text.indexOf(";")) + className;
            }
        }
        return className;
    }

    /**
     * Map an XML name to a Java identifier per the mapping rules of JSR 101 (in version 1.0 this is
     * "Chapter 20: Appendix: Mapping of XML Names"
     *
     * @param name is the mxml name
     * @return the java name per JSR 101 specification
     */
    public static String xmlNameToJava(String name) {
        // protect ourselves from garbage
        if (name == null || name.equals(""))
            return name;

        char[] nameArray = name.toCharArray();
        int nameLen = name.length();
        StringBuffer result = new StringBuffer(nameLen);
        boolean wordStart = false;

        // The mapping indicates to convert first character.
        int i = 0;
        while (i < nameLen && (isPunctuation(nameArray[i])
                || !Character.isJavaIdentifierStart(nameArray[i]))) {
            i++;
        }
        if (i < nameLen) {
            // Decapitalization code used to be here, but we use the
            // Introspector function now after we filter out all bad chars.

            result.append(nameArray[i]);
            // wordStart = !Character.isLetter(nameArray[i]);
            wordStart = !Character.isLetter(nameArray[i]) && nameArray[i] != "_".charAt(0);
        } else {
            // The identifier cannot be mapped strictly according to
            // JSR 101
            if (Character.isJavaIdentifierPart(nameArray[0])) {
                result.append("_" + nameArray[0]);
            } else {
                // The XML identifier does not contain any characters
                // we can map to Java. Using the length of the string
                // will make it somewhat unique.
                result.append("_" + nameArray.length);
            }
        }

        // The mapping indicates to skip over
        // all characters that are not letters or
        // digits. The first letter/digit
        // following a skipped character is
        // upper-cased.
        for (++i; i < nameLen; ++i) {
            char c = nameArray[i];

            // if this is a bad char, skip it and remember to capitalize next
            // good character we encounter
            if (isPunctuation(c) || !Character.isJavaIdentifierPart(c)) {
                wordStart = true;
                continue;
            }
            if (wordStart && Character.isLowerCase(c)) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }
            // If c is not a character, but is a legal Java
            // identifier character, capitalize the next character.
            // For example: "22hi" becomes "22Hi"
            // wordStart = !Character.isLetter(c);
            wordStart = !Character.isLetter(c) && c != "_".charAt(0);
        }

        // covert back to a String
        String newName = result.toString();

        // Follow JavaBean rules, but we need to check if the first
        // letter is uppercase first
        if (Character.isUpperCase(newName.charAt(0)))
            newName = Introspector.decapitalize(newName);

        // check for Java keywords
        if (isJavaKeyword(newName))
            newName = makeNonJavaKeyword(newName);

        return newName;
    } // xmlNameToJava

    /**
     * Is this an XML punctuation character?
     */
    private static boolean isPunctuation(char c) {
        return '-' == c || '.' == c || ':' == c || '\u00B7' == c || '\u0387' == c || '\u06DD' == c
                || '\u06DE' == c;
    } // isPunctuation

    /**
     * replace: Like String.replace except that the old new items are strings.
     *
     * @param name string
     * @param oldT old text to replace
     * @param newT new text to use
     * @return replacement string
     **/
    public static final String replace(String name, String oldT, String newT) {

        if (name == null)
            return "";

        // Create a string buffer that is twice initial length.
        // This is a good starting point.
        StringBuffer sb = new StringBuffer(name.length() * 2);

        int len = oldT.length();
        try {
            int start = 0;
            int i = name.indexOf(oldT, start);

            while (i >= 0) {
                sb.append(name.substring(start, i));
                sb.append(newT);
                start = i + len;
                i = name.indexOf(oldT, start);
            }
            if (start < name.length())
                sb.append(name.substring(start));
        } catch (NullPointerException e) {
        }

        return new String(sb);
    }

    /**
     * Used to cache a result from IsEnumClassSub(). Class->Boolean mapping.
     */
    private static WeakHashMap enumMap = new WeakHashMap();

    /**
     * Determine if the class is a JAX-RPC enum class. An enumeration class is recognized by a getValue() method, a
     * toString() method, a fromString(String) method a fromValue(type) method and the lack of a setValue(type) method
     */
    public static boolean isEnumClass(Class cls) {
        Boolean b = (Boolean) enumMap.get(cls);
        if (b == null) {
            b = (isEnumClassSub(cls)) ? Boolean.TRUE : Boolean.FALSE;
            synchronized (enumMap) {
                enumMap.put(cls, b);
            }
        }
        return b.booleanValue();
    }

    private static boolean isEnumClassSub(Class cls) {
        try {
            java.lang.reflect.Method[] methods = cls.getMethods();
            java.lang.reflect.Method getValueMethod = null, fromValueMethod = null,
                    setValueMethod = null, fromStringMethod = null;

            // linear search: in practice, this is faster than
            // sorting/searching a short array of methods.
            for (int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();

                if (name.equals("getValue") && methods[i].getParameterTypes().length == 0) { // getValue()
                    getValueMethod = methods[i];
                } else if (name.equals("fromString")) { // fromString(String s)
                    Object[] params = methods[i].getParameterTypes();
                    if (params.length == 1 && params[0] == String.class) {
                        fromStringMethod = methods[i];
                    }
                } else if (name.equals("fromValue") && methods[i].getParameterTypes().length == 1) { // fromValue(Something
                    // s)
                    fromValueMethod = methods[i];
                } else if (name.equals("setValue") && methods[i].getParameterTypes().length == 1) { // setValue(Something
                    // s)
                    setValueMethod = methods[i];
                }
            }

            // must have getValue and fromString, but not setValue
            // must also have toString(), but every Object subclass has that, so
            // no need to check for it.
            if (null != getValueMethod && null != fromStringMethod) {
                return !(null != setValueMethod && setValueMethod.getParameterTypes().length == 1
                        && getValueMethod.getReturnType() == setValueMethod.getParameterTypes()[0]);
            } else {
                return false;
            }
        } catch (SecurityException e) {
            return false;
        } // end of catch
    }

    public static String stackToString(Throwable e) {
        java.io.StringWriter sw = new java.io.StringWriter(1024);
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    /**
     * Tests the String 'value': return 'false' if its 'false', '0', or 'no' - else 'true'
     * <p>
     * Follow in 'C' tradition of boolean values: false is specific (0), everything else is true;
     */
    public static final boolean isTrue(String value) {
        return !isFalseExplicitly(value);
    }

    /**
     * Tests the String 'value': return 'true' if its 'true', '1', or 'yes' - else 'false'
     */
    public static final boolean isTrueExplicitly(String value) {
        return value != null && (value.equalsIgnoreCase("true") || value.equals("1")
                || value.equalsIgnoreCase("yes"));
    }

    /**
     * Tests the Object 'value': if its null, return default. if its a Boolean, return booleanValue() if its an Integer,
     * return 'false' if its '0' else 'true' if its a String, return isTrueExplicitly((String)value). All other types
     * return 'true'
     */
    public static final boolean isTrueExplicitly(Object value, boolean defaultVal) {
        if (value == null)
            return defaultVal;
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue() != 0;
        }
        if (value instanceof String) {
            return isTrueExplicitly((String) value);
        }
        return true;
    }

    public static final boolean isTrueExplicitly(Object value) {
        return isTrueExplicitly(value, false);
    }

    /**
     * Tests the Object 'value': if its null, return default. if its a Boolean, return booleanValue() if its an Integer,
     * return 'false' if its '0' else 'true' if its a String, return 'false' if its 'false', 'no', or '0' - else 'true'
     * All other types return 'true'
     */
    public static final boolean isTrue(Object value, boolean defaultVal) {
        return !isFalseExplicitly(value, !defaultVal);
    }

    public static final boolean isTrue(Object value) {
        return isTrue(value, false);
    }

    /**
     * Tests the String 'value': return 'true' if its 'false', '0', or 'no' - else 'false'
     * <p>
     * Follow in 'C' tradition of boolean values: false is specific (0), everything else is true;
     */
    public static final boolean isFalse(String value) {
        return isFalseExplicitly(value);
    }

    /**
     * Tests the String 'value': return 'true' if its null, 'false', '0', or 'no' - else 'false'
     */
    public static final boolean isFalseExplicitly(String value) {
        return value == null || value.equalsIgnoreCase("false") || value.equals("0")
                || value.equalsIgnoreCase("no");
    }

    /**
     * Tests the Object 'value': if its null, return default. if its a Boolean, return !booleanValue() if its an
     * Integer, return 'true' if its '0' else 'false' if its a String, return isFalseExplicitly((String)value). All
     * other types return 'false'
     */
    public static final boolean isFalseExplicitly(Object value, boolean defaultVal) {
        if (value == null)
            return defaultVal;
        if (value instanceof Boolean) {
            return !((Boolean) value).booleanValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue() == 0;
        }
        if (value instanceof String) {
            return isFalseExplicitly((String) value);
        }
        return false;
    }

    public static final boolean isFalseExplicitly(Object value) {
        return isFalseExplicitly(value, true);
    }

    /**
     * Tests the Object 'value': if its null, return default. if its a Boolean, return booleanValue() if its an Integer,
     * return 'false' if its '0' else 'true' if its a String, return 'false' if its 'false', 'no', or '0' - else 'true'
     * All other types return 'true'
     */
    public static final boolean isFalse(Object value, boolean defaultVal) {
        return isFalseExplicitly(value, defaultVal);
    }

    public static final boolean isFalse(Object value) {
        return isFalse(value, true);
    }

    /**
     * Given the MIME type string, return the Java mapping.
     */
    public static String mimeToJava(String mime) {
        if ("image/gif".equals(mime) || "image/jpeg".equals(mime)) {
            return "java.awt.Image";
        } else if ("text/plain".equals(mime)) {
            return "java.lang.String";
        } else if ("text/xml".equals(mime) || "application/mxml".equals(mime)) {
            return "javax.mxml.transform.Source";
        } else if ("application/octet-stream".equals(mime)
                || "application/octetstream".equals(mime)) {
            return "org.apache.axis.attachments.OctetStream";
        } else if (mime != null && mime.startsWith("multipart/")) {
            return "javax.mail.internet.MimeMultipart";
        } else {
            return "javax.activation.DataHandler";
        }
    } // mimeToJava

    // avoid testing and possibly failing everytime.
    private static boolean checkForAttachmentSupport = true;
    private static boolean attachmentSupportEnabled = false;

    /**
     * Makes the value passed in <code>initValue</code> unique among the {@link String} values contained in
     * <code>values</code> by suffixing it with a decimal digit suffix.
     */
    public static String getUniqueValue(Collection values, String initValue) {

        if (!values.contains(initValue)) {
            return initValue;
        } else {

            StringBuffer unqVal = new StringBuffer(initValue);
            int beg = unqVal.length(), cur, end;
            while (Character.isDigit(unqVal.charAt(beg - 1))) {
                beg--;
            }
            if (beg == unqVal.length()) {
                unqVal.append('1');
            }
            cur = end = unqVal.length() - 1;

            while (values.contains(unqVal.toString())) {

                if (unqVal.charAt(cur) < '9') {
                    unqVal.setCharAt(cur, (char) (unqVal.charAt(cur) + 1));
                } else {

                    while (cur-- > beg) {
                        if (unqVal.charAt(cur) < '9') {
                            unqVal.setCharAt(cur, (char) (unqVal.charAt(cur) + 1));
                            break;
                        }
                    }

                    // See if there's a need to insert a new digit.
                    if (cur < beg) {
                        unqVal.insert(++cur, '1');
                        end++;
                    }
                    while (cur < end) {
                        unqVal.setCharAt(++cur, '0');
                    }

                }

            }

            return unqVal.toString();

        } /* For else clause of selection-statement If(!values ... */

    } /* For class method JavaUtils.getUniqueValue */
}
