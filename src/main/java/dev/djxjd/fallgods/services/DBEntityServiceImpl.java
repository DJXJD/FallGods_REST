package dev.djxjd.fallgods.services;

import java.util.List;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.repositories.DBEntityRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class DBEntityServiceImpl<T extends DBEntity<T>> implements DBEntityService<T> {

	private DBEntityRepository<T> tRepo;
	private List<DBEntityService<?>> services;

	@Override
	public T getElement(Long id) {
		return tRepo.findById(id).orElse(null);
	}

	@Override
	public List<T> getCollection() {
		return tRepo.findAll();
	}

	@Override
	public Long addElement(T t) {
		return tRepo.save(t.setId(null)).getId();
	}

	@Override
	public Long replaceElement(T t) {
		if (!tRepo.existsById(t.getId())) return null;
		return tRepo.save(t).getId();
	}

	@Override
	public List<Long> replaceCollection(List<T> ts, boolean cascade) {
		ts.forEach(t -> t.setId(null));
		deleteCollection(cascade);
		return tRepo.saveAll(ts).stream().map(t -> t.getId()).toList();
	}

	@Override
	public Boolean deleteElement(Long id, boolean cascade) {
		if (!tRepo.existsById(id)) return false;
		List<List<? extends DBEntity<?>>> detachedChildren = detachChildrenReferencingElement(id);
		tRepo.deleteById(id);
		if (cascade) cascadeDelete(detachedChildren);
		return true;
	}

	@Override
	public void deleteCollection(boolean cascade) {
		List<List<? extends DBEntity<?>>> detachedChildren = detachChildrenReferencingCollection();
		tRepo.deleteAll();
		if (cascade) cascadeDelete(detachedChildren);
	}
	
	private void cascadeDelete(List<List<? extends DBEntity<?>>> dbEntityLists) {
		if (dbEntityLists == null) return;
		for (int i = 0; i < dbEntityLists.size(); i++) {
			for (DBEntity<?> e : dbEntityLists.get(i)) {
				services.get(i).deleteElement(e.getId(), true);
			}
		}
	}
	
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		return null;
	}

	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return null;
	}

}
