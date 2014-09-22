package market.bl.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import market.model.order.Product;

public class XmlMarketDao implements IMarketDao {
    
    private String db_filename = DEFAULT_DB_FILENAME;
    
    public static final String DEFAULT_DB_FILENAME = "build/resource/db_junit.xml";
    
    public XmlMarketDao() {
	init();
    }
    
    public XmlMarketDao(String fileName) {
	db_filename = fileName;
	init();
    }
    private void init() {
	try {
	    FileWriter writer = new FileWriter(new File(db_filename));
	    writer.flush();
	    saveToFile(new RootNode());
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void saveOrUpdate(Object object) {
	try {
	    RootNode rootNode = getFromFile();
	    
	    String fieldName = object.getClass().getSimpleName();
	    List fieldList = (List<?>) rootNode.getClass().getDeclaredField(fieldName).get(rootNode);
	    if (fieldList == null) {
		rootNode.getClass().getDeclaredField(fieldName).set(rootNode, new ArrayList());
		fieldList = (List<?>) rootNode.getClass().getDeclaredField(fieldName).get(rootNode);
	    }

	    Iterator<?> iter = fieldList.iterator();
	    while (iter.hasNext()) {
		Object fieldObject = iter.next();
		if (object.getClass().getDeclaredField("id").getInt(object) 
			== fieldObject.getClass().getDeclaredField("id").getInt(fieldObject)) {
		    iter.remove();
		    break;
		}
	    }
	    //TODO: ADD
	    fieldList.add(object);
	    
	    saveToFile(rootNode);
	    System.out.println("Object saved: " + object);
	} catch (JAXBException e) {
	    throw new RuntimeException(e);
	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}

    }

    @Override
    public <T> T get(int id, Class<T> objectClass) {
	try {
	    RootNode rootNode = getFromFile();
	    String fieldName = objectClass.getSimpleName();
	    List fieldList = (List<?>) rootNode.getClass().getDeclaredField(fieldName).get(rootNode);
	    if (fieldList == null) {
		rootNode.getClass().getDeclaredField(fieldName).set(rootNode, new ArrayList());
		fieldList = (List<?>) rootNode.getClass().getDeclaredField(fieldName).get(rootNode);
	    }

	    if (fieldList == null) {
		return null;
	    }
	    for (Object fieldObject : fieldList) {
		if (id == fieldObject.getClass().getDeclaredField("id").getInt(fieldObject)) {
		    return (T) fieldObject;
		}
	    }
	    return null;
	} catch (JAXBException e) {
	    throw new RuntimeException(e);
	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}

    }

    @Override
    public void remove(int id, Class<?> objectClass) {
	try {
	    JAXBContext jc = JAXBContext.newInstance(RootNode.class);
	    StreamSource xml = new StreamSource(new File(db_filename));
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    JAXBElement<RootNode> je1 = unmarshaller.unmarshal(xml, RootNode.class);
	    RootNode rootNode = je1.getValue();
	    
	    
	    String fieldName = objectClass.getSimpleName();
	    List fieldList = (List<?>) rootNode.getClass().getDeclaredField(fieldName).get(rootNode);
	    if (fieldList == null) {
		rootNode.getClass().getDeclaredField(fieldName).set(rootNode, new ArrayList());
		fieldList = (List<?>) rootNode.getClass().getDeclaredField(fieldName).get(rootNode);
	    }

	    if (fieldList == null) {
		return;
	    }
	    Iterator<?> iter = fieldList.iterator();
	    while (iter.hasNext()) {
		Object fieldObject = iter.next();
		if (id == fieldObject.getClass().getDeclaredField("id").getInt(fieldObject)) {
		    iter.remove();
		    saveToFile(rootNode);
		    return;
		}
	    }

	} catch (JAXBException e) {
	    throw new RuntimeException(e);
	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }
    
    
    private void saveToFile(RootNode rootNode) {
	try {
	    JAXBContext jc = JAXBContext.newInstance(RootNode.class);
	    JAXBElement<?> je2 = new JAXBElement(new QName(RootNode.class.getSimpleName()), RootNode.class, rootNode);
	    Marshaller marshaller = jc.createMarshaller();
//	    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    OutputStream out = new FileOutputStream(new File(db_filename), false);
	    marshaller.marshal(je2, out);
	} catch (JAXBException e) {
	    throw new RuntimeException(e);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	}
    }
    
    private RootNode getFromFile() throws JAXBException {
	    JAXBContext jc = JAXBContext.newInstance(RootNode.class);
	    StreamSource xml = new StreamSource(new File(db_filename));
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    JAXBElement<RootNode> je1 = unmarshaller.unmarshal(xml, RootNode.class);
	    RootNode rootNode = je1.getValue();
	    return rootNode;
    }

}
