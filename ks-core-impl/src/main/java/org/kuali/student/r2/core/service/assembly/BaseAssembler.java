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
package org.kuali.student.r2.core.service.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kuali.student.r1.common.dao.CrudDao;

import org.kuali.student.r1.common.entity.Attribute;
import org.kuali.student.r1.common.entity.AttributeOwner;
import org.kuali.student.r1.common.entity.Meta;
import org.kuali.student.r1.common.entity.MetaEntity;
import org.kuali.student.r1.common.entity.RichText;
import org.kuali.student.r1.common.entity.Type;
import org.kuali.student.r1.common.entity.Version;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.MetaInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.util.RichTextHelper;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.versionmanagement.dto.VersionInfo;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.springframework.beans.BeanUtils;

public class BaseAssembler {

    final static Logger logger = Logger.getLogger(BaseAssembler.class);

    public static List<AttributeInfo> toAttributeList(List<? extends Attribute<?>> attributes) {

        if(attributes == null) {
            return new ArrayList<AttributeInfo>(0);
        }

        List<AttributeInfo> attributeInfos = new ArrayList<AttributeInfo>();

        for (Attribute<?> attribute : attributes) {
            AttributeInfo attributeInfo = new AttributeInfo();
            attributeInfo.setKey(attribute.getName());
            attributeInfo.setValue(attribute.getValue());
            attributeInfos.add(attributeInfo);
        }

        return attributeInfos;
    }

    public static <A extends Attribute<O>, O extends AttributeOwner<A>> List<A> toGenericAttributes(Class<A> attributeClass, List<AttributeInfo> attributeList, O owner, CrudDao dao) throws InvalidParameterException {

        List<A> updatedAttributes = new ArrayList<A>();

        if (owner.getAttributes() == null) {
            owner.setAttributes(new ArrayList<A>());
        }

        Map<String, A> currentAttributes = new HashMap<String, A>();

        // Find all the old attributes(if the owner is not null)
        for (A attribute : owner.getAttributes()) {
            currentAttributes.put(attribute.getId(), attribute);
        }

        //Clear out the attributes
        owner.getAttributes().clear();

        if (attributeList==null) {
            return updatedAttributes;
        }

        //Update anything that exists, or create a new attribute if it doesn't
        for (AttributeInfo attr : attributeList) {
            A attribute = null;
            if (attr.getId() != null) {
                if (!currentAttributes.containsKey(attr.getId())) {
                    throw new InvalidParameterException(attr.getId());

                }
                attribute = currentAttributes.remove(attr.getId());
                attribute.setName(attr.getKey());
                attribute.setValue(attr.getValue());
                updatedAttributes.add(attribute);
                continue;
            }
            try {
                attribute = attributeClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error copying attributes.", e);
            }
            attribute.setName(attr.getKey());
            attribute.setValue(attr.getValue());
            attribute.setOwner(owner);
            updatedAttributes.add(attribute);
        }
        //TODO: Delete leftovers here if behavior is desired        
        return updatedAttributes;
    }

    /**
     * @param <S> Type Class
     * @param typeEntity the typeEntity to copy from
     * @return a new TypeInfo
     */
    public static <S extends Type<?>> TypeInfo toGenericTypeInfo(S typeEntity) {
        if (typeEntity == null) {
            return null;
        }

        TypeInfo info;
        try {
            // Create a new TypeInfo based on the <T> class and copy the
            // properties
            info = new TypeInfo();
            BeanUtils.copyProperties(typeEntity, info,
                    new String[] { "attributes", "descr" });

        info.setKey(typeEntity.getId());

        // Copy the attributes
        info.setAttributes(toAttributeList(typeEntity.getAttributes()));
        //Copy the description
        info.setDescr(new RichTextHelper().fromPlain(typeEntity.getDescr()));
        return info;

        } catch (Exception e) {
            logger.error("Exception occured: ", e);
        }

        return null;
    }

    public static <S extends Type<?>> List<TypeInfo> toGenericTypeInfoList(List<S> typeEntities) {
        List<TypeInfo> infos = new ArrayList<TypeInfo>();
        if (typeEntities != null) {
            for (S typeEntity : typeEntities) {
                infos.add(toGenericTypeInfo(typeEntity));
            }
        }
        return infos;
    }

    public static List<String> toGenericTypeKeyList(List<? extends Type<?>> typeEntities) {
        List<String> typeKeys = new ArrayList<String>();
        if (typeEntities != null) {
            for (Type<?> typeEntity : typeEntities) {
                typeKeys.add(typeEntity.getId());
            }
        }
        return typeKeys;
    }

    protected static MetaInfo toMetaInfo(MetaEntity metaEntity) {
        if (metaEntity == null) {
            return null;
        }
        return toMetaInfo(metaEntity.getMeta(), metaEntity.getVersionNumber());
    }

    protected static MetaInfo toMetaInfo(Meta meta, Long versionInd) {

        MetaInfo metaInfo = new MetaInfo();
        // If there was a meta passed in then copy the values
        if (meta != null) {
            BeanUtils.copyProperties(meta, metaInfo);
        }
        if (versionInd == null) {
            metaInfo.setVersionInd(null);
        } else {
            metaInfo.setVersionInd(versionInd.toString());
        }

        return metaInfo;
    }

    public static <T extends RichText> T toRichText(Class<T> richTextClass, RichTextInfo richTextInfo) {
        if (richTextInfo == null) {
            return null;
        }

        T richText = null;

        try {
            richText = richTextClass.newInstance();
            BeanUtils.copyProperties(richTextInfo, richText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return richText;
    }

    public static RichTextInfo toRichTextInfo(RichText entity) {
        if (entity == null) {
            return null;
        }

        RichTextInfo dto = new RichTextInfo();
        dto.setFormatted(entity.getFormatted());
        dto.setPlain(entity.getPlain());
        return dto;
    }

    public static VersionInfo toVersionInfo(Version version) {
        if (version == null) {
            return null;
        }
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setCurrentVersionStart(version.getCurrentVersionStart());
        versionInfo.setCurrentVersionEnd(version.getCurrentVersionEnd());
        versionInfo.setSequenceNumber(version.getSequenceNumber());
        versionInfo.setVersionComment(version.getVersionComment());
        versionInfo.setVersionIndId(version.getVersionIndId());
        versionInfo.setVersionedFromId(version.getVersionedFromId());
        return versionInfo;
    }
}
