package dev.djxjd.fallgods.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.services.DBEntityService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class DBEntityController<T extends DBEntity<T>> {

	@NonNull
	private DBEntityService<T> tService;
	private Boolean defaultCascade = true;

	@GetMapping({ "/{id}", "/{id}/" })
	public T getElement(@PathVariable Long id) {
		return unproxyLazyFields(tService.getElement(id));
	}

	@GetMapping({ "", "/" })
	public List<T> getCollection() {
		return unproxyLazyFields(tService.getCollection());
	}

	@PostMapping(value = { "", "/" }, consumes = "application/json")
	public Long postElement(@RequestBody T t) {
		return tService.addElement(t);
	}

	@PutMapping(value = { "/{id}", "/{id}/" }, consumes = "application/json")
	public Long putElement(@PathVariable Long id, @RequestBody T t) {
		return tService.replaceElement(t.setId(id));
	}

	@PutMapping(value = { "", "/" }, consumes = "application/json")
	public List<Long> putCollection(@RequestBody List<T> ts, @RequestParam(required = false) Boolean cascade) {
		if (cascade == null) cascade = defaultCascade;
		return tService.replaceCollection(ts, cascade);
	}

	@DeleteMapping({ "/{id}", "/{id}/" })
	public Boolean deleteElement(@PathVariable Long id, @RequestParam(required = false) Boolean cascade) {
		if (cascade == null) cascade = defaultCascade;
		return tService.deleteElement(id, cascade);
	}

	@DeleteMapping({ "", "/" })
	public void deleteCollection(@RequestParam(required = false) Boolean cascade) {
		if (cascade == null) cascade = defaultCascade;
		tService.deleteCollection(cascade);
	}
	
	protected T unproxyLazyFields(T t) {
		return t;
	}
	
	protected List<T> unproxyLazyFields(List<T> ts) {
		return ts.stream().map(this::unproxyLazyFields).toList();
	}

}
