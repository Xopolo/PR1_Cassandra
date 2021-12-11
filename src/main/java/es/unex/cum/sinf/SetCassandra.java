package es.unex.cum.sinf;

import java.util.HashSet;
import java.util.Iterator;

public class SetCassandra<E> extends HashSet<E> {
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : "'" + e + "'");
            if (!it.hasNext())
                return sb.append("}").toString();
            sb.append(",");
        }
    }
}
