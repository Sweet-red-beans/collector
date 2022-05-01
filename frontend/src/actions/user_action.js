import axios from "axios";
import { LOGIN_USER } from "./types";

const login = (body) => {
    const request = axios.post('http://localhost:8080/signin', body)
    .then(response => response.data);

    return {
        type: LOGIN_USER,
        user: request,
    }
}

export default login;