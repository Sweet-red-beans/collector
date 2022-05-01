import axios from "axios";
import { EVENTS } from "./types";

const events = (body) => {
    const request = axios.get('http://localhost:8080/events/search',{
        params: {
                    body
                }
    })
    .then(response => response.data);

    return {
        type: EVENTS,
        events: request,
    }
}

export default events;