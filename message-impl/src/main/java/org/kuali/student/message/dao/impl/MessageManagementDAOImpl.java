package org.kuali.student.message.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.kuali.student.message.MessageException;
import org.kuali.student.message.dao.MessageManagementDAO;
import org.kuali.student.message.dto.Message;
import org.kuali.student.message.dto.MessageList;
import org.kuali.student.message.entity.MessageEntity;
import org.kuali.student.message.service.impl.MessageServiceImpl;
import org.kuali.student.message.service.impl.util.POJOConverter;

@Repository
public class MessageManagementDAOImpl implements MessageManagementDAO {
    private EntityManager entityManager;
    
    final static Logger logger = LoggerFactory.getLogger(MessageManagementDAOImpl.class);
    
	private static boolean populated = false;
	
	private String xmlFile = "/testMessageData.xml";
	private static final String CONTEXT_NAME = "org.kuali.student.message.dto";
	private JAXBContext context;
	private Unmarshaller unmarshaller;
    
    @PersistenceContext(name = "Message")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
		 if (!populated) {
		        populated = true;
		        if (entityManager != null) {
		    		try {
		    			context = JAXBContext.newInstance(CONTEXT_NAME);
		    			unmarshaller = context.createUnmarshaller();
		    			MessageList messageList = (MessageList)unmarshaller.unmarshal(MessageServiceImpl.class.getResource(xmlFile));
		    			System.out.println("**************SIZE:" + messageList.getMessages().size() + "    " + messageList.getMessages().get(0).getValue());
		    	        List<Message> messages =  messageList.getMessages();
		    		    List<MessageEntity> messageEntities =  POJOConverter.mapList(messages, MessageEntity.class);
		    		    for(MessageEntity me: messageEntities){
		    		    	System.out.println("**************VALUE: " + me.getValue());
		    		    	this.addMessage(me);
		    		    }
		    		}
		    		catch (JAXBException e) {
		    				logger.error("Message test data instantiation failed.", e);
		    				throw new MessageException("Message test data instantiation failed.", e);
		    		}
		        }
		    }
    }
    
    public EntityManager getEntityManager(){
        return this.entityManager; 
    }
    
    public void addMessage(MessageEntity me){
    	try{
	        entityManager.persist(me);
    	}
		catch(Exception e){
		    throw new MessageException("adding message data query failed.", e);
		}
    }
    
    public int getTotalMessages(){
    	int count = 0;
    	try{
	        Query query = entityManager.createQuery("SELECT COUNT(m) FROM MessageEntity m");
	        count = Integer.parseInt((String)query.getSingleResult());
    	}
        catch(Exception e){
	    	logger.error("count query failed.", e);
			throw new MessageException("count query failed.", e);
	    }
        System.out.println("+++++++++++++++++++++++++++++++Count is " + count);
        return count;
    }
    
	public List<String> getLocales() {
		List<String> locales = new ArrayList<String>();
    	try{
	        Query query = entityManager.createQuery("SELECT distinct locale FROM MessageEntity m");
	        locales = (List<String>) query.getResultList();
    	}
        catch(Exception e){
	    	logger.error("getLocales query failed.", e);
			throw new MessageException("getLocales query failed.", e);
	    }
		return locales;
	}
	
	public List<String> getMessageGroups() {
		List<String> groups = new ArrayList<String>();
    	try{
	        Query query = entityManager.createQuery("SELECT distinct groupName FROM MessageEntity m");
	        groups = (List<String>) query.getResultList();
    	}
        catch(Exception e){
	    	logger.error("getMessageGroups query failed.", e);
			throw new MessageException("getMessageGroups query failed.", e);
	    }
		return groups;
	}
	
	public MessageEntity getMessage(String locale, String groupKey, String messageKey) {

		MessageEntity entity = null;
		try{
			Query query = entityManager.createQuery(
			        "select m from MessageEntity m " +
			        "where m.locale = :locale and " +
			        "m.groupName = :groupKey and " +
			        "m.id = :messageKey");
			 query.setParameter("locale", locale);
			 query.setParameter("groupKey", groupKey);
			 query.setParameter("messageKey", messageKey);
			 
			 if(!query.getResultList().isEmpty()){
			    entity = (MessageEntity)query.getResultList().get(0);
			 }else{
			    return null;
			 }
		}
		catch(Exception e){
			logger.error("getMessage query failed.", e);
			throw new MessageException("getMessage query failed.", e);
		}
		return entity;
	}
	

	
	public List<MessageEntity> getMessages(String locale, String groupKey) {
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		try{
			Query query = entityManager.createQuery(
		            "select m from MessageEntity m " +
		            "where m.locale = :locale and " +
		            "m.groupName = :groupKey ");
			 query.setParameter("locale", locale);
			 query.setParameter("groupKey", groupKey);
			 list = (List<MessageEntity>)query.getResultList();
		}
		catch(Exception e){
			logger.error("getMessages query failed.", e);
			throw new MessageException("getMessages query failed.", e);
		}
		return list;
	}
	
	public List<MessageEntity> getMessagesByGroups(String locale, List<String> groupKeys) {
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		try{
			for(String currentKey: groupKeys){
				Query query = entityManager.createQuery(
			            "select m from MessageEntity m " +
			            "where m.locale = :locale and " +
			            "m.groupName = :groupKey ");
				 query.setParameter("locale", locale);
				 query.setParameter("groupKey", currentKey);
				 list.addAll((List<MessageEntity>)query.getResultList());
			}
		}
		catch(Exception e){
			logger.error("getMessagesByGroups query failed.", e);
			throw new MessageException("getMessagesByGroups query failed.", e);
		}
		return list;
	}
	
	//assuming all keys are staying the same
	public MessageEntity updateMessage(String locale, String groupKey, String messageKey, MessageEntity updatedMessage) {
		MessageEntity returnValue = null;
    	try{
	    	MessageEntity theEntity = this.getMessage(locale, groupKey, messageKey);
	        if(theEntity != null){
        		theEntity.setValue(updatedMessage.getValue());
        		theEntity.setId(updatedMessage.getId());
        		theEntity.setLocale(updatedMessage.getLocale());
        		theEntity.setGroupName(updatedMessage.getGroupName());
        		entityManager.merge(theEntity);
        		returnValue = theEntity;
        	}
    	}
        catch(Exception e){
			throw new MessageException("updateMessage query failed.", e);
        }
        return returnValue;
	}
    

}
