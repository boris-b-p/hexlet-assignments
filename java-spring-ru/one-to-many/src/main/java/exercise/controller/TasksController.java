package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    @GetMapping(path = "")
    public List<TaskDTO> index() {
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(p -> taskMapper.map(p))
                .toList();
    }

    @GetMapping(path = "/{id}")
    public TaskDTO show(@PathVariable long id) {
        var post = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found" + id));
        var taskDTO = taskMapper.map(post);
        return taskDTO;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        var user = userRepository.findById(taskData.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User with not found"));
        task.setAssignee(user);
        taskRepository.save(task);
        var taskDto = taskMapper.map(task);
        return taskDto;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@RequestBody @Valid TaskUpdateDTO taskData, @PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        var user =  userRepository.findById(taskData.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        taskMapper.update(taskData, task);
        task.setAssignee(user);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long id) {
        taskRepository.deleteById(id);
    }
    // END
}
