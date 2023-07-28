import './todopage.scss'
import { Task } from '../../components/index'
import {useEffect, useState} from 'react'
import axios from 'axios'

import {useLocalState} from '../../util/useLocalStorage'
const ToDoPage = () => {

  const [task , setTask] = useState('');
  const [jwt , setJwt] = useLocalState("", "jwt")
  const [tasks , setTasks] = useState([]);
  const [refreshTasks, setRefreshTasks] = useState(false);

  const handleTaskChange = (event) => {
    setTask(event.target.value)
  }


  const handleSaveTask = async (event) => {
    event.preventDefault();
    const inputTask = document.querySelector('.task-input');
    inputTask.value = '';
    const payload = {
      task
    }
    const headers = {
      Authorization: `Bearer ${jwt.jwt}`
    };
    try{
        const response = await axios.post('http://localhost:8080/api/v1/task/create_task', payload,{ headers });
        const data = response.data;
        console.log(data);
        setRefreshTasks(true);
    } catch (error) {
      console.log(error);
    }

  };
 

  useEffect(() => {

    const  getAllTasks = async () => {
      const headers = {
        Authorization: `Bearer ${jwt.jwt}`
      };
      try{
        const response = await axios.get('http://localhost:8080/api/v1/task/get/all', { headers });
        const mappedData = response.data.map(task => ({
          id: task.id,
          task: task.task,
          status: task.done === true ? 'done' : 'not done'
        }));
        setTasks(mappedData);

      } catch (error) {
        const responseError = error.response.data.errors;
        console.log(responseError);
      }
    }
    
    getAllTasks();
    if(refreshTasks)
    {
      setRefreshTasks(false);
    }
  },[refreshTasks]);
  return (
    <div className='todo-page'>
      <div className='todo-page__container'>
        <div className='todo-page__container__title'>
          <strong>To Do App</strong>
        </div>
        <div className='todo-page__container__form'>
          <input  className='task-input' type="text" placeholder='Add a new task' onChange={handleTaskChange}/>
          <button className='submit-task-btn' onClick={handleSaveTask}>Save</button>
        </div>
        <div className='todo-page__container__tasks'>
        <table className="table mb-4">
              <thead>
                <tr>
                  <th scope="col">No.</th>
                  <th scope="col">Todo item</th>
                  <th scope="col">Status</th>
                  <th scope="col">Actions</th>
                </tr>
              </thead>
              <tbody className='table-body-tasks'>
                
                {tasks.map((task,i) => <Task key={task.id} data={{number :i+1 , task :task.task , status : task.status , id : task.id}}/>)}
              </tbody>
            </table>
        </div>
      </div>
    </div>
  )
}

export default ToDoPage
