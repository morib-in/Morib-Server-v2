package org.morib.server.domain.mset.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.mset.infra.MsetRepository;
import org.springframework.stereotype.Component;


/**
 * 추후 회의 후 DataService 같은 네이밍을 변경할 예정
 * 추가로 진행할
 */
public interface MsetDataService {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
