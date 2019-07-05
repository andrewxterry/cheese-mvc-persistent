package org.launchcode.models.data;

import org.launchcode.models.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuDao extends CrudRepository<Menu, Integer> {
}
