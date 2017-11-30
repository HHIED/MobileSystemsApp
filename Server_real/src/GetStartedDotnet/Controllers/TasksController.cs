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
                List<Models.Task> tasks = _dbContext.Tasks.Include("Creator").Include("Accepter").OrderByDescending(x => x.Score).Where(x => x.CompletedImage == null).Where(x=>x.IsApproved==false).Where(x=>x.IsTaken==false).ToList();
                JsonSerializerSettings jsonSerializerSettings = new JsonSerializerSettings();
                jsonSerializerSettings.TypeNameHandling = TypeNameHandling.Arrays;
                JsonResult result = Json(tasks, jsonSerializerSettings);
                return result;
            }
            
        }
        [Route("mytasks/{userId}")]
        [HttpGet("{userId}")]
        public ActionResult GetUserTasks(int userId)
        {
            if (_dbContext == null)
            {
                return Json("No database");
            }
            else
            {
                List<Models.Task> tasks = _dbContext.Tasks.Include("Creator").Include("Accepter").Where(x => x.Creator.Id == userId).Where(x=>x.IsApproved==false).ToList();
                JsonSerializerSettings jsonSerializerSettings = new JsonSerializerSettings();
                jsonSerializerSettings.TypeNameHandling = TypeNameHandling.Arrays;
                JsonResult result = Json(tasks, jsonSerializerSettings);
                return result;
            }

        }

        [Route("{taskId}")]
        [HttpGet("{taskID}")]
        public ActionResult GetTask(int taskId)
        {
            Models.Task task = _dbContext.Tasks.Include("Creator").Include("Accepter").SingleOrDefault(x => x.Id == taskId);
            return Json(task);
        }

        // POST api/values
        [Route("create/{creatorId}")]
        [HttpPost("{creatorId}")]
        public ActionResult CreateTask([FromBody] TaskMinimal taskMin, int creatorId)
        {
            if (_dbContext == null)
            {
                return Json("No database");
            }
            else
            {
                
                User creator = _dbContext.Users.Find(creatorId);
                User accepter = null;
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

        [Route("finishtask/{accepterId}")]
        [HttpPost("{accepterId}")]
        public ActionResult FinishTask([FromBody] TaskMinimal taskMin, int accepterId)
        {
            Models.Task task = _dbContext.Tasks.Find(taskMin.Id);
            task.CompletedImage = taskMin.CompletedImage;
            _dbContext.SaveChanges();
            return Json(task);

        }

        [Route("approveTask/{taskId}")]
        [HttpPost("{taskId}")]
        public void ApproveTask(int taskId)
        {
            Models.Task task = _dbContext.Tasks.Find(taskId);
            task.IsApproved = true;
            _dbContext.SaveChanges();
        }

        [Route("declineTask/{taskId}")]
        [HttpPost("{taskId}")]
        public void DeclineTask(int taskId)
        {
            Models.Task task = _dbContext.Tasks.Find(taskId);
            task.CompletedImage = null;
            _dbContext.SaveChanges();
        }

        [Route("takeTask/{taskId}")]
        [HttpPost("{taskId}")]
        public void AcceptTask(int taskId)
        {
            Models.Task task = _dbContext.Tasks.Find(taskId);
            task.IsTaken = true;
            _dbContext.SaveChanges();
        }

        [Route("abandonTask/{taskId}")]
        [HttpPost("{taskId}")]
        public void AbandonTask(int taskId)
        {
            Models.Task task = _dbContext.Tasks.Find(taskId);
            task.IsTaken = false;
            _dbContext.SaveChanges();
        }
    }
}
