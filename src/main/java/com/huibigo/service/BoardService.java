package com.huibigo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huibigo.model.BoardEntity;
import com.huibigo.persistence.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	private BoardRepository repository;
	
	public BoardEntity create(final BoardEntity entity) {
		
		//validation
		validate(entity);
		
		BoardEntity createdBoard = repository.save(entity);	
		log.info("Entity Id : {} is saved", entity.getId());
		
		return createdBoard;
	}
	
	public List<BoardEntity> retrieve() {
		return repository.findAll();
	}
	
	public List<BoardEntity> retrieveOptional(final BoardEntity entity) {
		
		// (1) 검색조건에 따른 자료들을 검색한다.
		if(entity.getCategory() != null) return repository.findByCategory(entity.getCategory());
		else return repository.findByCategory(entity.getCategory());
	}
	
	public BoardEntity update(final BoardEntity entity) {
		// (1) 저장할 엔티티가 유요한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		validate(entity);
		
		// (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트할 수 없기 때문이다.
		final Optional<BoardEntity> original = repository.findByUserId(entity.getWriter().getId());
		
		original.ifPresent(board -> {
		// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
			board.setTitle(entity.getTitle());
			board.setContent(entity.getContent());
			board.setUpdateTime(LocalDateTime.now());
		
		// (4) 데이터베이스에 새 값을 저장한다.
			repository.save(board);
		});
		
		// 2.3.2 Retrieve Todo에서 만든 메서드를 이용해 유저의 모든 Todo 리스트를 리턴한다.
		return original.get();
	}

	public void delete(final BoardEntity entity) {
		// (1) 저장할 엔티티가 유요한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		validate(entity);
		
		try {
		// (2) 엔티티를 삭제한다.
			repository.deleteById(entity.getId());
		} catch (Exception e) {
		// (3) exception 발생 시 id와 exception을 로깅한다.
			log.error("error deleting entity ", entity.getId(), e);
			
		// (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화하기 위해 e를 리턴하지 않고 새 exception오브젝트를 리턴한다.
			throw new RuntimeException("error deleting entity" + entity.getId());
		}
	}	
	
	private void validate(final BoardEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null");
		}
		
		if(entity.getWriter() == null) {
			log.warn("Unknown writer.");
			throw new RuntimeException("Unknown writer.");
		}
	}
}