using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace GetStartedDotnet.Models
{
    public class Task
    {
        [Key]
        public int Id { get; set; }
        public string Description { get; set; }
        public string Image { get; set; }
        public int Score { get; set; }

        public Task()
        {

        }

    }
}
