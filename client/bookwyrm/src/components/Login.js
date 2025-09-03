import React, { useState } from "react";
import { Container, Form, Button, Alert } from "react-bootstrap";
import "./css/Login.css"; // Optional, for additional styling

function Login() {
  const [user, setUser] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  // Handle login form submission
  const handleLogin = async (username, password) => {
    // setLoading(true);
    // setError(null);

    // Perform the login request
    await fetch("http://localhost:8080/api/user/authenticate", {
      // Corrected URL
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Login failed");
        }
        return response.json();
      })
      .then((data) => {
        // Store the JWT token in localStorage or cookies
        localStorage.setItem("jwt_token", data.jwt_token);
        setLoading(false);
        // Redirect to a protected page or reload the page
        console.log(data.jwt_token);
        window.location.reload();
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  };
  const handleSubmit = (e) => {
    console.log(user);
    e.preventDefault();

    handleLogin(user.username, user.password);
  };
  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  return (
    <Container style={{ marginTop: "250px" }}>
      <h2>Login</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formUsername">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            name="username" // Add name attribute
            value={user.username} // Use user.username
            onChange={handleChange} // Update onChange handler
            required
          />
        </Form.Group>
        <Form.Group controlId="formPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Enter password"
            name="password" // Add name attribute
            value={user.password} // Use user.password
            onChange={handleChange} // Update onChange handler
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit" disabled={loading}>
          {loading ? "Logging in..." : "Login"}
        </Button>
        {error && <Alert variant="danger">{error}</Alert>}
      </Form>
    </Container>
  );
}

export default Login;
