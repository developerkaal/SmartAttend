package com.smartattend.backend.classes;

import com.smartattend.backend.auth.UserAccount;
import com.smartattend.backend.auth.UserAccountRepository;
import com.smartattend.backend.students.StudentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;

    public ClassController(ClassRepository classRepository,
                           StudentRepository studentRepository,
                           UserAccountRepository userAccountRepository) {
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping
    public List<ClassResponse> listClasses() {
        return classRepository.findAll().stream()
            .map(cls -> new ClassResponse(
                cls.getId(),
                cls.getName(),
                cls.getDescription(),
                cls.getCreatedAt(),
                studentRepository.findByClassEntityId(cls.getId()).size()
            ))
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassResponse createClass(
        @Valid @RequestBody ClassRequest request,
        @RequestHeader(name = "X-User-Id", required = false) Long userId
    ) {
        UserAccount createdBy = userId == null ? null : userAccountRepository.findById(userId).orElse(null);
        ClassEntity saved = classRepository.save(new ClassEntity(request.name(), request.description(), createdBy));
        return new ClassResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getCreatedAt(), 0);
    }

    @PutMapping("/{id}")
    public ClassResponse updateClass(@PathVariable Long id, @Valid @RequestBody ClassRequest request) {
        ClassEntity cls = classRepository.findById(id).orElseThrow();
        cls.setName(request.name());
        cls.setDescription(request.description());
        ClassEntity saved = classRepository.save(cls);
        int count = studentRepository.findByClassEntityId(saved.getId()).size();
        return new ClassResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getCreatedAt(), count);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClass(@PathVariable Long id) {
        classRepository.deleteById(id);
    }

    public record ClassRequest(
        @NotBlank String name,
        String description
    ) {}

    public record ClassResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        int studentCount
    ) {}
}
