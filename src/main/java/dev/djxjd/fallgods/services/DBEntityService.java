package dev.djxjd.fallgods.services;

import java.util.List;

import dev.djxjd.fallgods.beans.DBEntity;

public interface DBEntityService<T extends DBEntity<T>> {
	
	public T getElement(Long id);
	public List<T> getCollection();
	public Long addElement(T t);
	public Long replaceElement(T t);
	public List<Long> replaceCollection(List<T> ts, boolean cascade);
	public Boolean deleteElement(Long id, boolean cascade);
	public void deleteCollection(boolean cascade);
	
	public T deriveTransientFields(T t);

}
