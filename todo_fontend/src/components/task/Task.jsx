import './task.scss'
import axios from 'axios'

import {useLocalState} from '../../util/useLocalStorage'
function Task({ data }) {
    
  const [jwt , setJwt] = useLocalState("", "jwt")
    const handleDeleteTask = async (event) => {

        event.preventDefault();

        const headers = {
            Authorization: `Bearer ${jwt.jwt}`
          };
          try{
            await axios.delete(`http://localhost:8080/api/v1/task/delete/task_id/${data.id}`,{ headers });
        
        } catch (error) {
          const responseError = error.response.data.errors;
          console.log(responseError);
        }
    };

    const handleFinishedTask = async (event) => {
        event.preventDefault();
    
        const headers = {
            Authorization: `Bearer ${jwt.jwt}`
        };
    
        try {
            const response = await fetch(`http://localhost:8080/api/v1/task/modify/finish/task_id/${data.id}`, {
                method: 'PUT',
                headers: {
                    ...headers,
                    'Content-Type': 'application/json' // Assuming you are sending JSON data
                },
                // You can pass any data to be sent in the request body here, if required
            });
    
            // Check if the request was successful (status code 2xx)
            if (response.ok) {
                console.log('Task finished successfully!');
            } else {
                // If the response has an error status code, handle it accordingly
                const responseError = await response.json(); // Parse the error response as JSON
                console.log(responseError.errors);
            }
        } catch (error) {
            console.log('Error occurred:', error);
        }
    };
    return (
        <tr className='task'>
            <th scope="row">{data.number}</th>
            <td>{data.task}</td>
            <td>{data.status}</td>
            <td className='action-btns'>
                <button type="submit" className="btn btn-danger" onClick={handleDeleteTask}>Delete</button>
                <button type="submit" className="btn btn-success" onClick={handleFinishedTask}>Finished</button>
            </td>
        </tr>

    )
}

export default Task
