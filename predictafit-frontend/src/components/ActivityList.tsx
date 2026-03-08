import { Card, CardContent, Grid, Typography } from "@mui/material"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getActivities } from "../services/api"

type Activity = {
  activityId: string
  activityType: string
  durationMinutes: number
  caloriesBurned: number
}

const ActivityList = () => {
  const [activities, setActivities] = useState<Activity[]>([])
  const navigate = useNavigate()

  const fetchActivities = async () => {
    try {
      const response = await getActivities()
      console.log(response.data)
      setActivities(response.data)
    } catch (error) {
      console.error("Failed to fetch activities:", error)
    }
  }

  useEffect(() => {
    fetchActivities()
  }, [])

  return (
    <Grid container spacing={2}>
      {activities.map((activity) => (
        <Grid key={activity.activityId} size={{ xs: 12, sm: 6, md: 4 }}>
          <Card
            sx={{ cursor: "pointer" }}
            onClick={() => navigate(`/activities/${activity.activityId}`)}
          >
            <CardContent>
              <Typography variant="h6">{activity.activityType}</Typography>
              <Typography>Duration: {activity.durationMinutes}</Typography>
              <Typography>Calories: {activity.caloriesBurned}</Typography>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  )
}

export default ActivityList