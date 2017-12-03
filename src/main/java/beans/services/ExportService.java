package beans.services;

import java.util.List;

public interface ExportService<T> {

    String exportEntities(List<T> entities);
}
