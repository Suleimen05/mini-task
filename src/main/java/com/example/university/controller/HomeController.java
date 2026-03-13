package com.example.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="ru">
            <head>
                <meta charset="UTF-8">
                <title>University Management System</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body { font-family: 'Segoe UI', sans-serif; background: #1a1a2e; color: #eee; padding: 20px; }
                    h1 { text-align: center; color: #e94560; margin-bottom: 10px; font-size: 28px; }
                    .subtitle { text-align: center; color: #888; margin-bottom: 30px; }
                    .container { max-width: 1200px; margin: 0 auto; display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
                    .card { background: #16213e; border-radius: 12px; padding: 20px; border: 1px solid #0f3460; }
                    .card h2 { color: #e94560; margin-bottom: 15px; font-size: 18px; }
                    .btn { padding: 8px 16px; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; margin: 4px; color: white; }
                    .btn-post { background: #27ae60; }
                    .btn-get { background: #2980b9; }
                    .btn-delete { background: #c0392b; }
                    .btn:hover { opacity: 0.85; }
                    #output { background: #0a0a1a; border: 1px solid #0f3460; border-radius: 8px; padding: 15px;
                              font-family: 'Consolas', monospace; font-size: 13px; white-space: pre-wrap;
                              min-height: 200px; max-height: 400px; overflow-y: auto; margin-top: 10px; color: #0f0; }
                    .relations { background: #16213e; border-radius: 12px; padding: 20px; border: 1px solid #0f3460;
                                 grid-column: 1 / -1; margin-top: 10px; }
                    .relations h2 { color: #e94560; margin-bottom: 10px; }
                    table { width: 100%%; border-collapse: collapse; }
                    th, td { padding: 8px 12px; text-align: left; border-bottom: 1px solid #0f3460; }
                    th { color: #e94560; }
                    .tag { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px; margin-right: 4px; }
                    .tag-one { background: #2980b9; }
                    .tag-many { background: #8e44ad; }
                    .tag-mtm { background: #d35400; }
                    .log-area { grid-column: 1 / -1; }
                </style>
            </head>
            <body>
                <h1>University Management System</h1>
                <p class="subtitle">Spring Boot 4 + JPA + H2 | REST API Demo</p>

                <div class="container">
                    <div class="card">
                        <h2>Students</h2>
                        <button class="btn btn-post" onclick="createStudent()">POST Create Student + Profile</button>
                        <button class="btn btn-get" onclick="getStudent()">GET Student by ID</button>
                        <button class="btn btn-delete" onclick="deleteStudent()">DELETE Student</button>
                        <button class="btn btn-post" onclick="createStudent2()">POST Create Student #2</button>
                    </div>

                    <div class="card">
                        <h2>Courses & Lessons</h2>
                        <button class="btn btn-post" onclick="createCourse()">POST Create Course</button>
                        <button class="btn btn-post" onclick="createCourse2()">POST Create Course #2</button>
                        <button class="btn btn-post" onclick="addLesson()">POST Add Lesson to Course</button>
                        <button class="btn btn-get" onclick="getCourse()">GET Course by ID</button>
                    </div>

                    <div class="card">
                        <h2>Many-to-Many</h2>
                        <button class="btn btn-post" onclick="assignCourse(1,1)">Assign Student 1 -> Course 1</button>
                        <button class="btn btn-post" onclick="assignCourse(1,2)">Assign Student 1 -> Course 2</button>
                        <button class="btn btn-post" onclick="assignCourse(2,1)">Assign Student 2 -> Course 1</button>
                    </div>

                    <div class="card">
                        <h2>Cascade Demo</h2>
                        <button class="btn btn-delete" onclick="deleteStudent1()">DELETE Student 1 (cascade test)</button>
                        <button class="btn btn-get" onclick="getCourse1After()">GET Course 1 (still exists?)</button>
                    </div>

                    <div class="relations">
                        <h2>Entity Relationships</h2>
                        <table>
                            <tr><th>Relationship</th><th>Type</th><th>Entities</th><th>Key Annotations</th></tr>
                            <tr><td><span class="tag tag-one">OneToOne</span></td><td>Student <-> StudentProfile</td>
                                <td>1 student = 1 profile</td><td>cascade=ALL, orphanRemoval=true</td></tr>
                            <tr><td><span class="tag tag-many">OneToMany</span></td><td>Course <-> Lesson</td>
                                <td>1 course = many lessons</td><td>cascade=ALL, @JsonManagedReference</td></tr>
                            <tr><td><span class="tag tag-mtm">ManyToMany</span></td><td>Student <-> Course</td>
                                <td>many students <-> many courses</td><td>@JoinTable, @JsonIgnore</td></tr>
                        </table>
                    </div>

                    <div class="log-area">
                        <h2 style="color:#e94560;margin-bottom:10px;">API Response Log</h2>
                        <div id="output">Click any button to test the API...</div>
                    </div>
                </div>

                <script>
                    const out = document.getElementById('output');

                    async function api(method, url, body) {
                        out.textContent = `>>> ${method} ${url}\\n`;
                        if (body) out.textContent += `Body: ${JSON.stringify(body, null, 2)}\\n\\n`;
                        try {
                            const opts = { method, headers: {'Content-Type':'application/json'} };
                            if (body) opts.body = JSON.stringify(body);
                            const res = await fetch(url, opts);
                            const status = res.status;
                            let text = '';
                            try { const json = await res.json(); text = JSON.stringify(json, null, 2); } catch(e) { text = '(no body)'; }
                            out.textContent += `Status: ${status}\\n${text}`;
                        } catch(e) { out.textContent += `Error: ${e.message}`; }
                    }

                    function createStudent() {
                        api('POST', '/students', {
                            firstName: 'Ivan', lastName: 'Petrov', email: 'ivan@mail.ru',
                            profile: { address: 'Moscow, Tverskaya 1', phone: '+7-999-111-22-33', birthDate: '2000-05-15' }
                        });
                    }
                    function createStudent2() {
                        api('POST', '/students', {
                            firstName: 'Anna', lastName: 'Smirnova', email: 'anna@mail.ru',
                            profile: { address: 'Saint-Petersburg, Nevsky 10', phone: '+7-999-222-33-44', birthDate: '2001-09-20' }
                        });
                    }
                    function getStudent() { api('GET', '/students/1'); }
                    function deleteStudent() { api('DELETE', '/students/1'); }
                    function createCourse() { api('POST', '/courses', { title: 'Mathematics', credits: 5 }); }
                    function createCourse2() { api('POST', '/courses', { title: 'Physics', credits: 4 }); }
                    function addLesson() { api('POST', '/courses/1/lessons', { topic: 'Linear Algebra', duration: 90 }); }
                    function getCourse() { api('GET', '/courses/1'); }
                    function assignCourse(s, c) { api('POST', `/students/${s}/courses/${c}`); }
                    function deleteStudent1() { api('DELETE', '/students/1'); }
                    function getCourse1After() { api('GET', '/courses/1'); }
                </script>
            </body>
            </html>
            """;
    }
}
