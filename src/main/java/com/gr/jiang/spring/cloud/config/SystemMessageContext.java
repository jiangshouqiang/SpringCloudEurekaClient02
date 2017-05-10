package com.gr.jiang.spring.cloud.config;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * Created by jiang on 2017/5/9.
 */
public class SystemMessageContext {
    private String requestId;

    /**
     * Thread local storage used for locating the active message context. This information is only valid for the
     * lifetime of this request.
     */
    protected static ThreadLocal currentContext = new ThreadLocal();

    /**
     * 将指定的Context压栈到当前ThreadLocal中
     *
     * @param mc
     * @return void
     */
    public static void pushCurrentContext(SystemMessageContext mc) {
        Stack s;
        if (currentContext.get() == null) {
            s = new Stack();
            currentContext.set(s);
        } else {
            s = (Stack) currentContext.get();
        }
        s.push(mc);
    }

    /**
     * 弹出当前ThreadLocal的栈顶Context
     *
     * @return HiContext
     */
    public static SystemMessageContext popCurrentContext() {
        if (currentContext.get() != null) {
            Stack s = (Stack) currentContext.get();
            return (SystemMessageContext) s.pop();
        }
        return null;
    }

    /**
     * 获取当前ThreadLocal的栈顶Context.
     *
     * @return HiContext
     */
    public static SystemMessageContext getCurrentContext() {
        if (currentContext.get() != null) {
            Stack s = (Stack) currentContext.get();
            return (SystemMessageContext) s.peek();
        }
        return null;
    }

    /**
     * 设置当前ThreadLock 栈顶元素
     *
     * @param mc
     * @return void
     */
    public static void setCurrentContext(SystemMessageContext mc) {
        Stack s;
        if (currentContext.get() == null) {
            s = new Stack();
            currentContext.set(s);
        } else {
            s = (Stack) currentContext.get();
        }
        if (!s.isEmpty())
            s.pop();
        s.push(mc);
    }

    /**
     * 清空当前的ThreadLocal
     *
     * @return void
     */
    public static void removeCurrentContext() {
        // jdk1.4.2 not supported
        currentContext.remove();
        // currentContext.set(null);

    }

    /**
     * 全局静态区
     */
    protected static final SystemMessageContext RootContext = new SystemMessageContext(
            "RootContext");

    /**
     * 获得根Context
     *
     * @return
     */
    public static SystemMessageContext getRootContext() {
        return RootContext;
    }

    /**
     * 创建一个子Context
     *
     * @param id     Context id
     * @param parent 父Context,如果为null, 则父Context为根Context
     * @return 创建成功的Context
     */
    public static SystemMessageContext createContext(String id, SystemMessageContext parent) {
        if (parent == null)
            parent = RootContext;
        return new SystemMessageContext(id, parent);
    }

    /**
     * 创建一个子Context
     *
     * @param parent 父Context,如果为null, 则父Context为根Context
     * @return 创建成功的Context
     */
    public static SystemMessageContext createContext(SystemMessageContext parent) {
        if (parent == null)
            parent = RootContext;
        return new SystemMessageContext(null, parent);
    }

    /**
     * 创建一个子Context并绑定到当前线程
     *
     * @return 创建成功的Context
     */
    public static SystemMessageContext createAndPushContext() {
        SystemMessageContext parent = getCurrentContext();
        if (parent == null)
            parent = RootContext;

        SystemMessageContext mc = new SystemMessageContext(null, parent);
        pushCurrentContext(mc);
        return mc;
    }

    protected String id;

    /**
     * Storage for an arbitrary bag of properties associated with this MessageContext.
     */
    protected final LockableHashtable bag;// = new LockableHashtable();

    protected SystemMessageContext parent;

    protected SystemMessageContext firstChild;

    protected SystemMessageContext nextBrother;

    /**
     * Get a <code>String</code> property by name.
     *
     * @param propName the name of the property to fetch
     * @return the value of the named property
     * @throws ClassCastException if the property named does not have a <code>String</code> value
     */
    public String getStrProp(String propName) {
        return (String) getProperty(propName);
    }

    /**
     * Get a <code>String</code> property by name.
     *
     * @return the value of the named property
     * @throws ClassCastException if the property named does not have a <code>String</code> value
     */
    public String getStrProp(String key1, String key2) {
        String key = key1 + "." + key2;
        return (String) getProperty(key);
    }

    /**
     * Tests to see if the named property is set in the 'bag', returning <code>false</code> if it is not present at all.
     * This is equivalent to <code>isPropertyTrue(propName, false)</code>.
     *
     * @param propName the name of the property to check
     * @return true or false, depending on the value of the property
     */
    public boolean isPropertyTrue(String propName) {
        return isPropertyTrue(propName, false);
    }

    /**
     * Test if a property is set to something we consider to be true in the 'bag'.
     * <ul>
     * <li>If not there then <code>defaultVal</code> is returned.</li>
     * <li>If there, then...
     * <ul>
     * <li>if its a <code>Boolean</code>, we'll return booleanValue()</li>
     * <li>if its an <code>Integer</code>, we'll return <code>false</code> if its <code>0</code> else <code>true</code>
     * </li>
     * <li>if its a <code>String</code> we'll return <code>false</code> if its <code>"false"</code>" or <code>"0"</code>
     * else <code>true</code></li>
     * <li>All other types return <code>true</code></li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param propName   the name of the property to check
     * @param defaultVal the default value
     * @return true or false, depending on the value of the property
     */
    public boolean isPropertyTrue(String propName, boolean defaultVal) {
        return JavaUtils.isTrue(getProperty(propName), defaultVal);
    }

    /**
     * Allows you to set a named property to the passed in value. There are a few known properties (like username,
     * password, etc) that are variables in Call. The rest of the properties are stored in a Hashtable. These common
     * properties should be accessed via the accessors for speed/type safety, but they may still be obtained via this
     * method. It's up to one of the Handlers (or the Axis engine itself) to go looking for one of them.
     *
     * @param name  Name of the property
     * @param value Value of the property
     */
    public void setProperty(String name, Object value) {
        if (name == null || value == null) {
            return;
            // Is this right? Shouldn't we throw an exception like:
            // throw new IllegalArgumentException(msg);
        } else {
            bag.put(name.toUpperCase(), value);
        }
    } // setProperty

    /**
     * 删除当前Context指定的Key
     *
     * @param name
     */
    public void delProperty(String name) {
        bag.remove(name.toUpperCase());
    }

    /**
     * 设置当前Context指定key的值
     *
     * @param key1  key前缀
     * @param key2  key名称
     * @param value
     */
    public void setProperty(String key1, String key2, Object value) {
        setProperty(key1 + "." + key2, value);
    } // setProperty

    public void setProperty(String name, Object value, boolean locked) {
        if (name == null || value == null) {
            return;
            // Is this right? Shouldn't we throw an exception like:
            // throw new IllegalArgumentException(msg);
        } else {
            bag.put(name.toUpperCase(), value, locked);
        }
    } // setProperty

    public void setProperty(String key1, String key2, Object value, boolean locked) {
        setProperty(key1 + key2, value, locked);
    } // setProperty

    /**
     * Returns true if the MessageContext contains a property with the specified name.
     *
     * @param name Name of the property whose presense is to be tested
     * @return Returns true if the MessageContext contains the property; otherwise false
     */
    public boolean containsProperty(String name) {
        Object propertyValue = getProperty(name);
        return (propertyValue != null);
    }

    /**
     * Returns an <code>Iterator</code> view of the names of the properties in this <code>MessageContext</code>.
     *
     * @return an <code>Iterator</code> over all property names
     */
    public Iterator getPropertyNames() {
        // fixme: this is potentially unsafe for the caller - changing the
        // properties will kill the iterator. Consider iterating over a copy:
        // return new HashSet(bag.keySet()).iterator();
        return bag.keySet().iterator();
    }

    /**
     * Returns an Iterator view of the names of the properties in this MessageContext and any parents of the
     * LockableHashtable
     *
     * @return Iterator for the property names
     */
    public Iterator getAllPropertyNames() {
        return bag.getAllKeys().iterator();
    }

    /**
     * 返回当前Context指定Key的值，Key对大小写不敏感；若指定的Key不存在则返回null
     *
     * @param name - key名称
     * @return Object
     */
    public Object getProperty(String name) {
        if (name != null) {
            if (bag == null) {
                return null;
            } else {
                return bag.get(name.toUpperCase());
            }
        } else {
            return null;
        }
    }

    /**
     * 获取当前Context指定Key的值
     *
     * @param key1 key前缀
     * @param key2 key名称
     * @return
     */
    public Object getProperty(String key1, String key2) {
        String key = key1 + "." + key2;
        return getProperty(key);
    }

    /**
     * 获取当前Context的第一个子节点
     *
     * @return HiContext
     */
    public SystemMessageContext getFirstChild() {
        return firstChild;
    }

    /**
     * 获取当前Context的兄弟
     *
     * @return HiContext
     */
    public SystemMessageContext getNextBrother() {
        return nextBrother;
    }

    /**
     * 设置当前Context的 ID
     *
     * @param id - 当前Context的ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回当前Context的ID
     *
     * @return 当前Context的ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * only for create RootContext
     */
    protected SystemMessageContext(String id) {
        bag = new LockableHashtable();
        parent = null;
        this.id = id;
    }

    protected SystemMessageContext(String id, SystemMessageContext parent) {
        this.requestId = JrnGenerator.genMsgId();
        bag = new LockableHashtable();
        if (parent == null)
            parent = RootContext;

        this.bag.setParent(parent.bag);
        this.parent = parent;
        this.id = id;

        parent.addChild(this);
    }

    protected void addChild(SystemMessageContext child) {
        child.nextBrother = null;

        if (firstChild == null) {
            firstChild = child;
        } else {
            SystemMessageContext lastChild = firstChild;
            while (lastChild.nextBrother != null)
                lastChild = lastChild.nextBrother;
            lastChild.nextBrother = child;
        }
    }

    /**
     * 序列化当前的Context
     */
    public String toString() {
        StringBuffer rs = new StringBuffer();
        rs.append("id=" + id);
        rs.append(";");
        Set set = bag.entrySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            rs.append(iter.next());
            rs.append(";");
        }
        if (parent != null) {
            rs.append("parent=" + parent.id);
            rs.append(";");
        }
        if (firstChild != null) {
            rs.append("firstChild=" + firstChild.id);
            rs.append(";");
        }
        if (nextBrother != null) {
            rs.append("nextBrother=" + nextBrother.id);
            rs.append(";");
        }

        return rs.toString();
    }

    /**
     * 取当前Context的Parent
     *
     * @return HiContext
     */
    public SystemMessageContext getParent() {
        return parent;
    }

    /**
     * 清空当前的Context
     */
    public void clear() {
        // this.bag.clear();
        if (parent != null) {
            parent.delchild(this);
        }
    }

    private void delchild(SystemMessageContext context) {
        SystemMessageContext before = null;
        if (firstChild != null) {
            SystemMessageContext lastChild = firstChild;
            do {
                if (lastChild == context) {
                    if (before == null) {
                        firstChild = lastChild.nextBrother;
                    } else {
                        before.nextBrother = lastChild.nextBrother;
                    }
                    lastChild.nextBrother = null;
                }
                before = lastChild;
                lastChild = lastChild.nextBrother;
            } while (lastChild != null);
        }
    }

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }
}
