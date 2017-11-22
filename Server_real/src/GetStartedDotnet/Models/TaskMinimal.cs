using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace GetStartedDotnet.Models
{
    public class TaskMinimal
    {
        public int Id { get; set; }
        public string Description { get; set; }
        public byte[] Image { get; set; }
        public int Score { get; set; }
        public float Lattitude { get; set; }
        public float Longtitude { get; set; }

        public TaskMinimal()
        {

        }
    }
}
