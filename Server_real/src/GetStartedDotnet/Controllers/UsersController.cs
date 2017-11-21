using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using GetStartedDotnet.Models;
using System.Linq;
using System.Text.Encodings.Web;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace GetStartedDotnet.Controllers
{
    [Route("api/[controller]")]
    public class UsersController : Controller
    {
        private readonly HtmlEncoder _htmlEncoder;
        private readonly VisitorsDbContext _dbContext;

        public UsersController(HtmlEncoder htmlEncoder, VisitorsDbContext dbContext = null)
        {
            _dbContext = dbContext;
            _htmlEncoder = htmlEncoder;
        }

        // GET: api/values
        [HttpGet]
        public ActionResult Get()
        {
            if (_dbContext == null)
            {
                return Json("No database");
            }
            else
            {
                return Json(_dbContext.Users.ToList());
            }
        }

        // POST api/values
        [Route("/create")]
        [HttpPost]
        public ActionResult CreateUser()
        {
            if (_dbContext == null)
            {
                return Json("No database");
            }
            else
            {
                User u = new User();
                _dbContext.Users.Add(u);
                _dbContext.SaveChanges();
                return Json(u);
            }
        }
    }
}
