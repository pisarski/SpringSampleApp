package com.lumesse.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lumesse.entity.Spittle;
import com.lumesse.repository.SpittleRepository;
import com.lumesse.service.SpittleService;

@Service
public class SpittleServiceImpl implements SpittleService {

	@Autowired
	private SpittleRepository spittleRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Spittle> findAllSorted() {
		return spittleRepository.findAll().stream()
				.sorted((s1, s2) -> s1.getTime().compareTo(s2.getTime()))
				.collect(Collectors.toList());
	}

}
