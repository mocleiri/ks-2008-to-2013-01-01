/**
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.core.assembly.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.kuali.student.core.assembly.data.Data.DataType;
import org.kuali.student.core.assembly.data.Data.IntegerKey;
import org.kuali.student.core.assembly.data.Data.Key;

/**
 * @author wilj
 * @deprecated SimpleModelDefinition no longer works, use DataModelDefinition instead
 */
@Deprecated
public class SimpleModelDefinition implements ModelDefinition {
    private static final long serialVersionUID = 1L;

    public static class Definition implements Serializable{
        private static final long serialVersionUID = 1L;
        
        private Map<Key, Definition> children = new HashMap<Key, Definition>();
        private String className;

        public Definition() {

        }

        public Definition(String className) {
            this.className = className;
        }

        /**
         * @return the children
         */
        public Map<Key, Definition> getChildren() {
            return children;
        }

        /**
         * @return the className
         */
        public String getClassName() {
            if (className == null) {
                className = Data.class.getName();
            }
            return className;
        }

        /**
         * @param className
         *            the className to set
         */
        public void setClassName(final String className) {
            this.className = className;
        }

    }

    private Definition root = new Definition();

    public void define(final QueryPath path, final String className) {
        Definition current = root;
        for (Iterator<Key> itr = path.iterator(); itr.hasNext();) {
            final Key key = itr.next();
            if (itr.hasNext()) {
                Definition d = current.getChildren().get(key);
                if (d == null) {
                    d = new Definition();
                    current.getChildren().put(key, d);
                }
                current = d;
            } else {
                Definition d = new Definition(className);
                current.getChildren().put(key, d);
            }
        }
    }

    public void define(final String path, final String className) {
        define(QueryPath.parse(path), className);
    }

    @Override
    public void ensurePath(final Data root, final QueryPath path, final boolean includeLeafNode) {
        Data currentData = root;
        final Definition currentDef = this.root;

        for (final Iterator<Key> itr = path.iterator(); itr.hasNext();) {
            final Key key = itr.next();
            if (includeLeafNode || itr.hasNext()) {
                Data data = currentData.get(key);
                Definition def = currentDef.getChildren().get(key);
                if (def == null) {
                    def = currentDef.getChildren().get(Data.WILDCARD_KEY);
                }
                if (data == null) {
                    final String className = (def == null) ? Data.class.getName() : def.getClassName();
                    data = new Data(className);
                    currentData.set(key, data);
                }
                currentData = data;
            }
        }
    }

    /**
     * @see org.kuali.student.core.assembly.data.ModelDefinition#getType()
     */
    @Override
    public DataType getType(final QueryPath path) {
        Definition currentDef = this.root;

//        for (final Iterator<Key> itr = path.iterator(); itr.hasNext();) {
//            final Key key = itr.next();
//            if (itr.hasNext()) {
//                Definition def = currentDef.getChildren().get(key);
// 
//                if (def == null){
//                    if (key instanceof IntegerKey) {
//                        def = currentDef.getChildren().get(Data.WILDCARD_KEY);
//                    } else {
//                        throw new RuntimeException(key + " in " + path.toString() + " not defined in model definition");                        
//                    }
//                }
//                currentDef = def;
//            } else {
//                Definition def = currentDef.getChildren().get(key);
//                if (def == null) {
//                    // FIXME: Should all non-defined leaf nodes be treated as strings
//                    return "String";
//                } else {
//                    return def.getClassName();
//                }
//            }
//        }

        throw new RuntimeException(path.toString() + " not defined in model definition.");        
    }

	@Override
	public Metadata getMetadata(QueryPath path) {
		// TODO Auto-generated method stub
		return null;
	}
}
