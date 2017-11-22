using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using System.Text.Encodings.Web;
using GetStartedDotnet.Models;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace GetStartedDotnet.Controllers
{
    [Route("api/[controller]")]
    public class TasksController : Controller
    {

        private readonly HtmlEncoder _htmlEncoder;
        private readonly VisitorsDbContext _dbContext;

        public TasksController(HtmlEncoder htmlEncoder, VisitorsDbContext dbContext = null)
        {
            _dbContext = dbContext;
            _htmlEncoder = htmlEncoder;
        }
        // GET: /<controller>/
        [HttpGet]
        public ActionResult Get()
        {
            if (_dbContext == null)
            {
                return Json("No database");
            }
            else
            {
                List<Models.Task> tasks = _dbContext.Tasks.Include("Creator").Include("Accepter").ToList();
                foreach(Models.Task t in tasks)
                {
                    foreach(byte b in t.Image)
                    Console.WriteLine(b);
                }
                JsonSerializerSettings jsonSerializerSettings = new JsonSerializerSettings();
                jsonSerializerSettings.TypeNameHandling = TypeNameHandling.Arrays;
                JsonResult result = Json(tasks, jsonSerializerSettings);
                return result;
            }
            
        }

        // POST api/values
        [Route("create/{creatorId}/{accepterId}")]
        [HttpPost("{creatorId}/{accepterId}")]
        public ActionResult CreateTask([FromBody] TaskMinimal taskMin, int creatorId, int accepterId)
        {
            if (_dbContext == null)
            {
                return Json("No database");
            }
            else
            {
                
                User creator = _dbContext.Users.Find(creatorId);
                User accepter = _dbContext.Users.Find(accepterId);
                Models.Task task = new Models.Task(taskMin.Description, taskMin.Image, taskMin.Score, taskMin.Lattitude, taskMin.Longtitude, creator, accepter);
                _dbContext.Tasks.Add(task);
                _dbContext.SaveChanges();
                return Json(task);
            }
        }

        [Route("IncreaseScore")]
        [HttpPost]
        public ActionResult IncreaseScore([FromBody]Models.Task task)
        {
            Models.Task dbTask = _dbContext.Tasks.Find(task.Id);
            dbTask.Score++;
            _dbContext.SaveChanges();
            return Json(dbTask);
        }
    }
}
