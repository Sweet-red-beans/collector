import axios from "axios";
import { MAIN_EVENTS } from "./types";

const mainEvents = () => {
    const request = axios.get('http://localhost:8080/event-limit')
    .then(response => response.data);

    return {
        type: MAIN_EVENTS,
        events: request,
    }
}

export default mainEvents;