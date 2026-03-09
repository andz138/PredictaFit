import { Card, CardContent, Grid, Typography, Box, Skeleton, Stack } from "@mui/material"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getActivities } from "../services/api"
import DirectionsRunIcon from "@mui/icons-material/DirectionsRun"
import DirectionsBikeIcon from "@mui/icons-material/DirectionsBike"
import PoolIcon from "@mui/icons-material/Pool"
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter"
import SelfImprovementIcon from "@mui/icons-material/SelfImprovement"
import LocalFireDepartmentIcon from "@mui/icons-material/LocalFireDepartment"
import TimerIcon from "@mui/icons-material/Timer"

type Activity = {
  activityId: string
  activityType: string
  durationMinutes: number
  caloriesBurned: number
}

const ACTIVITY_ICONS: Record<string, React.ReactNode> = {
  RUNNING: <DirectionsRunIcon sx={{ fontSize: 40 }} />,
  CYCLING: <DirectionsBikeIcon sx={{ fontSize: 40 }} />,
  SWIMMING: <PoolIcon sx={{ fontSize: 40 }} />,
  WEIGHT_TRAINING: <FitnessCenterIcon sx={{ fontSize: 40 }} />,
  YOGA: <SelfImprovementIcon sx={{ fontSize: 40 }} />,
}

const ActivityList = () => {
  const [activities, setActivities] = useState<Activity[]>([])
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  const fetchActivities = async () => {
    setLoading(true)
    try {
      const response = await getActivities()
      setActivities(response.data)
    } catch (error) {
      console.error("Failed to fetch activities:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchActivities()
  }, [])

  if (loading) {
    return (
      <Grid container spacing={2}>
        {[1, 2, 3].map((item) => (
          <Grid key={item} size={{ xs: 12, sm: 6, md: 4 }}>
            <Skeleton variant="rectangular" height={200} sx={{ borderRadius: 1 }} />
          </Grid>
        ))}
      </Grid>
    )
  }

  if (activities.length === 0) {
    return (
      <Box
        sx={{
          textAlign: "center",
          py: 8,
          px: 2,
        }}
      >
        <FitnessCenterIcon sx={{ fontSize: 64, color: "text.secondary", mb: 2, opacity: 0.5 }} />
        <Typography variant="h5" color="text.secondary" gutterBottom>
          No activities yet
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Start by adding your first activity using the form above
        </Typography>
      </Box>
    )
  }

  return (
    <Grid container spacing={2}>
      {activities.map((activity) => (
        <Grid key={activity.activityId} size={{ xs: 12, sm: 6, md: 4 }}>
          <Card
            sx={{
              cursor: "pointer",
              transition: "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)",
              height: "100%",
              display: "flex",
              flexDirection: "column",
              "&:hover": {
                transform: "translateY(-8px)",
                boxShadow: "0 12px 24px rgba(0,0,0,0.15)",
              },
            }}
            onClick={() => navigate(`/activities/${activity.activityId}`)}
          >
            <CardContent sx={{ flex: 1 }}>
              <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 2 }}>
                <Box sx={{ color: "primary.main", display: "flex", alignItems: "center" }}>
                  {ACTIVITY_ICONS[activity.activityType] || ACTIVITY_ICONS.RUNNING}
                </Box>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  {activity.activityType.replace(/_/g, " ")}
                </Typography>
              </Box>

              <Stack spacing={1}>
                <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                  <TimerIcon sx={{ fontSize: 18, color: "text.secondary" }} />
                  <Typography variant="body2" color="text.secondary">
                    {activity.durationMinutes} min
                  </Typography>
                </Box>
                <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                  <LocalFireDepartmentIcon sx={{ fontSize: 18, color: "#ff6b6b" }} />
                  <Typography variant="body2">
                    <strong>{activity.caloriesBurned}</strong> cal
                  </Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  )
}

export default ActivityList