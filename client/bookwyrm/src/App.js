import React from 'react';
import { BrowserRouter as Router } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import Header from './components/Header';
import NotFound from './components/NotFound';
import Home from './components/Home';
import FindAClub from './components/FindAClub';
import CreateClub from './components/CreateClub';
import Reviews from './components/Reviews';
import CreateAReview from './components/CreateReview';
import YourClubs from './components/YourClubs';
import Discussions from './components/Discussions';
import Messages from './components/Messages';
import CreateDiscussion from './components/CreateDiscussion';
import MakeMessage from './components/MakeMessage';
import BookSearch from './components/BookSearch';
import EditClub from './components/EditClub';
import Book from './components/Book';

function App() {
  return (
    <Router>
      <Header/>
      <Routes>
        <Route path ="/findAClub" element={<FindAClub/>}/>
        <Route path ="/createAClub" element={<CreateClub/>}/>
        <Route path ="/createAReview" element={<CreateAReview/>}/>
        <Route path ="/reviews" element={<Reviews/>}/>
        <Route path ="/yourClubs" element={<YourClubs/>}/>
        <Route path ="/club/discussion/:id" element={<Discussions/>}/>
        <Route path ="/club/edit/:id" element={<EditClub/>}/>
        <Route path ="/discussion/messages/:id" element={<Messages/>}/>
        <Route path ="/createDiscussion/:id" element={<CreateDiscussion/>}/>
        <Route path ="/createMessage/:id" element={<MakeMessage/>}/>
        <Route path="*" element={<NotFound/>}/>
        <Route path="/" element={<Home />} />
        <Route path="/findAbook" element={<BookSearch />} />
        <Route path = "/book/:isbn" element={ <Book/>} /> 
      </Routes>
    </Router>
  )
}

export default App;