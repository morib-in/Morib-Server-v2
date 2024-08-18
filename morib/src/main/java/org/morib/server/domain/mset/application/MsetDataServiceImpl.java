package org.morib.server.domain.mset.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.mset.infra.MsetRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MsetDataServiceImpl implements MsetDataService{

    private final MsetRepository msetRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }
}
