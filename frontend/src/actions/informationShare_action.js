import axios from "axios";
import { INFO } from "./types";

const info = (body) => {
    const request = axios.get('http://localhost:8080/events/search',{
        params: {
                    body
                }
    })
    .then(response => response.data);

    return {
        type: INFO,
        info: body,
    }
}

export default info;