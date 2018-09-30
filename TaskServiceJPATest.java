package it.fds.taskmanager;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.fds.taskmanager.dto.TaskDTO;

/**
 * Basic test suite to test the service layer, it uses an in-memory H2 database. 
 * 
 * TODO Add more and meaningful tests! :)
 *
 * @author <a href="mailto:damiano@searchink.com">Damiano Giampaoli</a>
 * @since 10 Jan. 2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskServiceJPATest extends Assert{

    @Autowired
    
    TaskService taskService;
    
    @Test
    public void writeAndReadOnDB_Test1() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task1");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("Test task1", tOut.getTitle());
        List<TaskDTO> list = taskService.showList();
        assertEquals(9,list.size());
    }
    
    @Test
    public void GetStatus_Test2() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task2");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("Test task2", tOut.getTitle());
        List<TaskDTO> list = taskService.showList();
        assertEquals(t.getStatus(), "NEW");
  	t.setStatus(TaskState.RESOLVED.toString().toUpperCase());
	assertEquals(t.getStatus(), "RESOLVED");
 	t.setStatus(TaskState.POSTPONED.toString().toUpperCase());
	assertEquals(t.getStatus(), "POSTPONED");
  	t.setStatus(TaskState.RESTORED.toString().toUpperCase());
	assertEquals(t.getStatus(), "RESTORED");

    }
    
    @Test
    public void taskServiceFunctionResolved_Test31() {
    	TaskDTO tt = new TaskDTO();
    	tt.setTitle("Test task3");
    	TaskDTO tt1 = taskService.saveTask(tt);
    	TaskDTO tt2 = taskService.findOne(tt1.getUuid());
    	taskService.resolveTask(tt2.getUuid());
    	TaskDTO tt3 = taskService.findOne(tt2.getUuid());
    	assertEquals(tt3.getStatus(),"RESOLVED");
	
    }
    
    @Test
    public void taskServiceFunctionPostponed_Test32() {
    	TaskDTO tt = new TaskDTO();
    	tt.setTitle("Test task3");
    	TaskDTO tt1 = taskService.saveTask(tt);
    	TaskDTO tt2 = taskService.findOne(tt1.getUuid());
    	taskService.postponeTask(tt2.getUuid(), 5);
    	TaskDTO tt3 = taskService.findOne(tt2.getUuid());
    	assertEquals(tt3.getStatus(),"POSTPONED");
	

    }

    @Test
    public void taskServiceFunctionRestored_Test33() {
    	TaskDTO tt = new TaskDTO();
    	tt.setTitle("Test task3");
    	TaskDTO tt1 = taskService.saveTask(tt);
    	TaskDTO tt2 = taskService.findOne(tt1.getUuid());
    	taskService.postponeTask(tt2.getUuid(), 5);
    	TaskDTO tt3 = taskService.findOne(tt2.getUuid());
    	assertEquals(tt3.getStatus(),"POSTPONED");
	taskService.unmarkPostoned();
	TaskDTO tt4 = taskService.findOne(tt3.getUuid());
	assertEquals(tt4.getStatus(),"RESTORED");
	

    }


    @Test
    public void ExcludePostponed_Test4() {
        TaskDTO t0 = new TaskDTO();
        TaskDTO t1 = new TaskDTO();
        TaskDTO t2 = new TaskDTO();
        TaskDTO t3 = new TaskDTO();
        t0.setTitle("Test task0");
        t1.setTitle("Test task1");
        t2.setTitle("Test task2");
        t3.setTitle("Test task3");
        t0.setStatus(TaskState.NEW.toString().toUpperCase());
        t1.setStatus(TaskState.NEW.toString().toUpperCase());
        t2.setStatus(TaskState.RESOLVED.toString().toUpperCase());
        t3.setStatus(TaskState.NEW.toString().toUpperCase());
        taskService.saveTask(t0);
        taskService.saveTask(t1);
        taskService.saveTask(t2);
        taskService.saveTask(t3);
        List<TaskDTO> list = taskService.showList();
        assertEquals(4, list.size());
        t0.setStatus(TaskState.POSTPONED.toString().toUpperCase());
        t1.setStatus(TaskState.POSTPONED.toString().toUpperCase());
        t2.setStatus(TaskState.POSTPONED.toString().toUpperCase());
        t3.setStatus(TaskState.POSTPONED.toString().toUpperCase());
        taskService.showList();
        assertEquals(0,list.size());
      
       
    }
    
  
    
     

     

    @EnableJpaRepositories
    @Configuration
    @SpringBootApplication
    public static class EndpointsMain{}
}
