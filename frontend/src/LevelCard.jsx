function LevelCard({level, title, xp})
{
     async function startLevel() {
          console.log('Start Level clicked')
        }
return ( <section>
                      <h2>Level {level}: {title}</h2>
                      <h3>xp earned: {xp}</h3>
                      <button onClick={startLevel}>Start Level</button>
                  </section>
    )
}
export default LevelCard