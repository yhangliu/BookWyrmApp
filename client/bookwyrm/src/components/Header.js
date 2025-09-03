import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import logo from "./images/logo.jpg";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import "./css/Header.css";

function Header() {
  return (
    <Navbar expand="lg" className="background" fixed="top">
      <Container fluid>
        <Navbar.Brand>
          <img
            src={logo}
            className="d-inline-block align-top"
            alt="Bookwyrm logo"
          />
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link className="navlink" as={Link} to="/findAclub">
              Find a Club
            </Nav.Link>
            <Nav.Link className="navlink" as={Link} to="/yourclubs">
              Your Clubs
            </Nav.Link>
            <Nav.Link className="navlink" as={Link} to="/reviews">
              Reviews
            </Nav.Link>
            <Nav.Link className="navlink" as={Link} to="/findABook">
              Find a book
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
        <Nav className="ms-auto">
          <Button variant="light" className="me-2">
            <Link to={"/login"} className="nav-link">
              Sign in
            </Link>
          </Button>
          <Button variant="light">
            <Link to={"/register"} className="nav-link">
              Register
            </Link>
          </Button>
        </Nav>
      </Container>
    </Navbar>
  );
}

export default Header;
