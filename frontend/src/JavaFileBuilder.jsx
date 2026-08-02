import { useState } from 'react'

function JavaFileBuilder()
{
const [rows, setRows] = useState([1])
const[generatedFile, setGeneratedFile] = useState('')
const [message, setMessage] = useState('')
const [fileName, setFileName] = useState('')

function addRow()
{
setRows([...rows, rows.length + 1])
}

async function validateAndGenerateJavaFile()
{
    const request = {
         fileName: fileName,
              structures: [
                {
                  accessModifier: 'public',
                  className: 'First',
                  isMain: true
                }
              ]
        }
    const response =  await fetch('/v1/file-structure/generate', {
                           method: 'POST',
                           headers: {
                             'Content-Type': 'application/json'
                           },
                           body: JSON.stringify(request)
                         })

      const result = await response.json()
      setGeneratedFile(result.generatedCode)
      setMessage(result.fileName + ' generated successfully')

    }
    return (
        <section>
            <h2> Build Java File </h2>
            <form>
                <label>Java file name
                <input value={fileName} onChange={event => setFileName (event.target.value)}/>
                </label>
                  <button type="button" onClick={addRow}>
                      +
                    </button>
                {rows.map(row => (<div key={row}>
                <label>Access
                <select>
                    <option>public</option>
                    <option>default (package-private)</option>
                    </select>
                    </label>
                    <label>
                        Class name
                        <input />
                        </label>
                     <label>
                         <input type="checkbox" />
                         Include main method
                         </label>
                         </div>
                        ))}
                     <button type="button" onClick={validateAndGenerateJavaFile}>Generate Java File</button>
                </form>
                <p>{message}</p>
                <pre>
                    {generatedFile}</pre>
            </section>
        )
    }

export default JavaFileBuilder