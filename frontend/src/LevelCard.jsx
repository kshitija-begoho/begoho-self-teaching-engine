import {useState} from 'react'
import JavaFileBuilder from './JavaFileBuilder.jsx'

function LevelCard({level, title, xp})
{

const [started, setStarted] = useState(false)

     async function startLevel() {
          setStarted(true)
        }
return ( <section>
                      <h2>Level {level}: {title}</h2>
                      <h3>xp earned: {xp}</h3>
                      {!started && (<button onClick={startLevel}>Start Level</button>)}

                      {started && <JavaFileBuilder />}
                  </section>
    )
}
export default LevelCard