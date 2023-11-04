import React, { useState } from 'react'
import MainBoard from './MainBoard'
import Registration from './Registration'

const App = () => {
    const [token, setToken] = useState('true');
    if(token){
        return <MainBoard/>
    } 
    return <Registration/>
}

export default App