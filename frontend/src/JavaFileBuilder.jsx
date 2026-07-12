function JavaFileBuilder()
{
    return (
        <section>
            <h2> Build Java File </h2>
            <form>
                <label>Java file name
                <input/>
                </label>
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
                     <button type="button">Generate Java File</button>

                </form>
            </section>
        )
    }

export default JavaFileBuilder